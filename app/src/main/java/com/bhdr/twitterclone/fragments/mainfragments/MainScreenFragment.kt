package com.bhdr.twitterclone.fragments.mainfragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.bhdr.twitterclone.helperclasses.*
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

// TODO:ilk defa giriş yaptım : Room boş room boş ise apiye istek atıp tweetleri alıyorum tweetleri
//  adaptere gönderiyorum aynı zamanda tweetRoomAdd() fun gönderip rooma kaydediyorum
//TODO: uygulamaya giriş yaptım room dolu tweetsRoomModelConvertPostModel()fun roomu gönderip post modele çevirip adaptere gönderiyorum
//  buluta istek atıyorum yeni tweetler var ise bildirim gösteriyorum butona basar ise adaptere listi gönderiyorum yeni tweetleri gösteriyorum
// tweetRoomAdd() fun gönderip rooma kaydediyorum
// TODO: signarlR Aynı şekilde yeni tweet atılır ise dinleye bilri vs bakacaz
@Suppress("DEPRECATION")
class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
   TweetsAdapter.ClickedTweetListener {

   private val binding by viewBinding(FragmentMainScreenBinding::bind)
   private lateinit var viewModel: TweetViewModel
   private val tweetAdapter by lazy { TweetsAdapter(this) }
   private var userProfileClickListener: MainScreenInterFace? = null
   private var mutableFollowNewTweetHashMap: HashMap<Int, String> = HashMap()
   var userId: Int? = null
   private var tweetsRoom: List<TweetsRoomModel?> = mutableListOf()

   override fun onAttach(context: Context) {
      super.onAttach(context)

      if (context is MainScreenInterFace) {
         userProfileClickListener = context
      } else {
         throw RuntimeException(context.toString())
      }
   }

   // viewModel = ViewModelProvider(requireParentFragment())[TweetViewModel::class.java]

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      viewModel = ViewModelProvider(requireParentFragment())[TweetViewModel::class.java]
      userId = requireContext().userId()

      viewModelObservable()
      binding()
      netWorkControlAndCloudRequestTweets()
   }

   private fun viewModelObservable() {
      with(viewModel) {
         allRoomTweets.observe(viewLifecycleOwner) {
            if (it != null) {
               tweetAdapter.submitList(it)
               tweetsRoom = it
            }
         }
         mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
               TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()

               TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
               TweetRepository.MainStatus.DONE -> {
                  binding.lottiAnim.gone()
               }
            }
         }

         tweets.observe(viewLifecycleOwner) {
            isNewTweet(tweetsRoom, it)
         }

         mutableFollowNewTweet.observe(viewLifecycleOwner) {
            //SignalR dinliyor ve 2'den fazla tweet atılırsa clouddan tweetleri çekip tweetObserveden devam ediyor
            Log.e("try", it.size.toString())
            if (it!!.size >= 2) {
               viewModel.getTweets(userId!!)
               viewModel.mutableFollowNewTweet.value!!.clear()
            }
         }

      }


   }

   private fun binding() {
      with(binding) {
         tweetsRecyclerView.apply {
            layoutManager = LinearLayoutManager(
               tweetsRecyclerView.context,
               LinearLayoutManager.VERTICAL,
               false
            )
            adapter = tweetAdapter
         }

         profilePicture.setOnClickListener {
            userProfileClickListener?.openDrawerClick()

         }
         profilePicture.picasso(requireContext().userPhotoUrl())

         addTweetFAB.setOnClickListener {
            Navigation.findNavController(requireView())
               .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
         }
      }

   }


   private fun netWorkControlAndCloudRequestTweets() {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         if (requireContext().checkNetworkConnection()) {
            viewModel.getTweets(userId!!)
         }
      } else {
         viewModel.getTweets(userId!!)
      }
   }

   private fun isNewTweet(roomTweet: List<TweetsRoomModel?>, cloudTweet: List<Posts>) {
      try {
         if (roomTweet.isEmpty()) {
            newTweetButton(cloudTweet, true)
         } else {
            if (roomTweet.isNotEmpty() && cloudTweet.isNotEmpty()) {
               if (roomTweet.size != cloudTweet.size) {
                  roomTweet.forEach { itCloud ->
                     val tweet: Posts? = cloudTweet.find { itRoom ->
                        itCloud!!.id != itRoom.id
                     }
                     if (tweet != null) {
                        mutableFollowNewTweetHashMap[id] = tweet.user?.photoUrl.toString()
                     }
                  }
                  if (mutableFollowNewTweetHashMap.isNotEmpty()) {
                     newTweetButton(cloudTweet, false)
                  }
               }
            }
         }

      } catch (e: Exception) {
         Log.e("isNewTweet", e.toString())
      }
   }

   private fun newTweetButton(cloudTweet: List<Posts>, isNull: Boolean) {
      with(binding) {
         if (isNull) {
            viewModel.tweetsRoomConvertAndAdd(cloudTweet)

         } else {


            mutableFollowNewTweetHashMap.apply {
               if (this.size == 1) {
                  Picasso.get().load(this.values.toTypedArray()[0]).into(userPhoto1)
                  seeNewTweet.visible()
                  userPhoto1.visible()
               } else if (this.size == 2) {
                  Picasso.get().load(this.values.toTypedArray()[1]).into(userPhoto2)
                  userPhoto2.visible()
               } else if (this.size >= 3) {
                  seeNewTweet.visible()
                  Picasso.get().load(this.values.toTypedArray()[0]).into(userPhoto1)
                  userPhoto1.visible()
                  Picasso.get().load(this.values.toTypedArray()[1]).into(userPhoto2)
                  userPhoto2.visible()
                  Picasso.get().load(this.values.toTypedArray()[2]).into(userPhoto3)
                  userPhoto3.visible()
               }
            }
            seeNewTweet.setOnClickListener {

               seeNewTweet.gone()
               userPhoto1.gone()
               userPhoto2.gone()
               userPhoto3.gone()
               viewModel.tweetsRoomConvertAndAdd(cloudTweet)
            }
         }
      }
   }

   override fun crfButtonsListener(
      commentrtfav: String,
      tweetId: Int,
      currentlyCRFNumber: Int
   ) {
      when (commentrtfav) {
         "fav" -> viewModel.postLiked(tweetId, currentlyCRFNumber, requireContext().userId())
         // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
         // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)

      }
   }


   interface MainScreenInterFace {
      fun openDrawerClick()
   }

}



