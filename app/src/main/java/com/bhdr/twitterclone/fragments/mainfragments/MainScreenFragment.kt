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
import com.bhdr.twitterclone.models.SignalRModel
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.RoomViewModel
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.MainViewModel
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
   private val viewModel by lazy { TweetViewModel() }
   private val viewModelMain by lazy { MainViewModel() }
   private val tweetAdapter by lazy { TweetsAdapter(this) }
   private var mutableNotFollowTweetOrLikeList: List<SignalRModel>? = null
   var userProfileClickListener: MainScreenInterFace? = null
   private var mutableFollowNewTweetHashMap: HashMap<Int, String> = HashMap()
   var userId: Int? = null

   //Room Repoya taşınacak
   private lateinit var viewModelRoom: RoomViewModel
   private val tweetsRoom: MutableList<Posts> = mutableListOf()
   override fun onAttach(context: Context) {
      super.onAttach(context)

      if (context is MainScreenInterFace) {
         userProfileClickListener = context
      } else {
         throw RuntimeException(context.toString())
      }
   }


   private fun room() {

      viewModelRoom = ViewModelProvider(requireParentFragment())[RoomViewModel::class.java]

      viewModelRoom.allRoomTweets.observe(viewLifecycleOwner) {
         tweetsRoomModelConvertPostModel(it)
      }

   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      room()
      userId = requireContext().userId()
      //viewModelMain.getFollowedUserIdList(userId!!)


      binding.addTweetFAB.setOnClickListener {
         Navigation.findNavController(requireView())
            .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
      }


      viewModelObservable()
      binding.profilePicture.picasso(requireContext().userPhotoUrl())



      viewModelMain.mutableFollowNewTweet.observe(viewLifecycleOwner) {
         Log.e("try", it.size.toString())
         if (it!!.size == 1) {
            val url: String = it.values.toTypedArray()[0]
            Log.e("try", url)
            Picasso.get().load(url).into(binding.userPhoto1)
            Picasso.get().load(it.values.toTypedArray()[0]).into(binding.userPhoto2)
            binding.seeNewTweet.visible()
            binding.userPhoto1.visible()
            binding.userPhoto2.visible()
         } else if (it.size >= 3) {
            Picasso.get().load(it.values.toTypedArray()[2]).into(binding.userPhoto3)
            binding.userPhoto3.visible()
         }
      }

//      mutableFollowNewTweetHashMap = viewModelMain.mutableFollowNewTweet.value!!


//      mutableFollowNewTweetHashMap?.apply {
//         if (this.size == 1) {
//            val url: String = this.values.toTypedArray()[0]
//            Log.e("try", url)
//            Picasso.get().load(url).into(binding.userPhoto1)
//            Picasso.get().load(this.values.toTypedArray()[1]).into(binding.userPhoto2)
//            binding.seeNewTweet.visible()
//            binding.userPhoto1.visible()
//            binding.userPhoto2.visible()
//         } else if (this.size >= 3) {
//            Picasso.get().load(this.values.toTypedArray()[2]).into(binding.userPhoto3)
//            binding.userPhoto3.visible()
//         }
//      }
      // netWorkControlAndCloudRequestTweets()

   }

   private fun viewModelObservable() {

      viewModel.apply {
         mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
               TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()

               TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
               TweetRepository.MainStatus.DONE -> {
                  binding.lottiAnim.gone()
               }
            }
         }
//         tweets.observe(viewLifecycleOwner) {
//            tweetRoomAdd(it)
//            tweetAdapter.submitList(it)
//
//         }
      }
      viewModelMain.apply {
         mutableNotFollowTweetOrLikeList = mutableNotFollowTweetOrLike.value
         mutableNotFollowTweetOrLikeList.apply {
            //SearchaDAPTER
         }
         mutableNotFollowTweetOrLike.value?.apply {


         }


      }
      binding.tweetsRecyclerView.apply {
         layoutManager = LinearLayoutManager(
            binding.tweetsRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
         )
         adapter = tweetAdapter
      }

      binding.profilePicture.setOnClickListener {
         userProfileClickListener?.openDrawerClick()

      }

      viewModel.tweets.observe(viewLifecycleOwner) {
         isNewTweet(tweetsRoom, it)
      }
   }

   private fun tweetRoomAdd(it: List<Posts>) {
      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         it.forEach {
            var userRoomModel: UsersRoomModel? = null
            it.user?.apply {
               userRoomModel = UsersRoomModel(id, photoUrl, userName, name)
            }
            it.apply {
               tweetsAddRoom.add(
                  TweetsRoomModel(
                     date,
                     id,
                     postContent,
                     postImageUrl,
                     postLike,
                     userRoomModel,
                     userId
                  )
               )
            }
         }
         viewModelRoom.tweetInsert(tweetsAddRoom)
      } catch (e: Exception) {

         Log.e("ex", e.toString())
      }
   }

   private fun tweetsRoomModelConvertPostModel(it: List<TweetsRoomModel?>) {

      it.forEach { itFor ->
         var users: Users
         itFor?.user.apply {
            users = Users(
               id = id,
               photoUrl = this!!.photoUrl,
               userName = userName,
               name = name,
               date = null,
               email = null,
               followers = null,
               messages = null,
               phone = null,
               userPassword = null,
               posts = null
            )
         }
         itFor?.apply {
            tweetsRoom.add(
               Posts(
                  date = date,
                  id = id,
                  postContent = postContent,
                  postImageUrl = postImageUrl,
                  postLike = postLike,
                  user = users,
                  userId = userId,
                  followers = null,
                  tags = null
               )
            )
         }

      }
      tweetAdapter.submitList(tweetsRoom)
      netWorkControlAndCloudRequestTweets()
   }


   private fun netWorkControlAndCloudRequestTweets() {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

         if (requireContext().checkNetworkConnection()) {
            viewModel.getPosts(userId!!)

         }
      } else {
         viewModel.getPosts(userId!!)
      }


   }

   private fun isNewTweet(roomTweet: List<Posts>, cloudTweet: List<Posts>) {
      try {
         if (roomTweet.isNotEmpty() && cloudTweet.isNotEmpty()) {
            if (roomTweet.size != cloudTweet.size) {

               roomTweet.forEach { itCloud ->
                  var tweet: Posts? = null
                  tweet = cloudTweet.find { itRoom ->
                     itCloud.id != itRoom.id
                  }
                  if (tweet != null) {
                     mutableFollowNewTweetHashMap[tweet.id!!] = tweet.user?.photoUrl.toString()

                  }
               }
               if (mutableFollowNewTweetHashMap.isNotEmpty()) {
                  newTweetButton(cloudTweet)
               }
            }
         }
      } catch (e: Exception) {
         Log.e("isNewTweet", e.toString())
      }
   }

   private fun newTweetButton(cloudTweet:List<Posts>) {
      mutableFollowNewTweetHashMap.apply {
         if (this!!.size == 1) {
            val url: String = this.values.toTypedArray()[0]
            Log.e("try", url)
            Picasso.get().load(url).into(binding.userPhoto1)
            Picasso.get().load(this.values.toTypedArray()[0]).into(binding.userPhoto2)
            binding.seeNewTweet.visible()
            binding.userPhoto1.visible()
            binding.userPhoto2.visible()
         } else if (this.size >= 3) {
            Picasso.get().load(this.values.toTypedArray()[2]).into(binding.userPhoto3)
            binding.userPhoto3.visible()
         }
      }
      binding.seeNewTweet.setOnClickListener {
         tweetAdapter.submitList(cloudTweet)
      }
   }

   override fun crfButtonsListener(
      commentrtfav: String,
      tweetId: Int,
      currentlyCRFNumber: Int
   ) {
      when (commentrtfav) {
         "fav" -> viewModel.postLiked(tweetId, currentlyCRFNumber, requireContext())
         // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
         // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)

      }
   }


   interface MainScreenInterFace {
      fun openDrawerClick()
   }

}



