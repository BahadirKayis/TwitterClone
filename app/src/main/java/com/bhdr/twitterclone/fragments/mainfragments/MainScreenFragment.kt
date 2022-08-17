package com.bhdr.twitterclone.fragments.mainfragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.bhdr.twitterclone.helperclasses.*
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
   TweetsAdapter.ClickedTweetListener {

   private val binding by viewBinding(FragmentMainScreenBinding::bind)
   private lateinit var viewModel: TweetViewModel
   private val tweetAdapter by lazy { TweetsAdapter(this) }
   private var userProfileClickListener: MainScreenInterFace? = null

   var userId: Int = 0

   override fun onAttach(context: Context) {
      super.onAttach(context)
      if (context is MainScreenInterFace) {
         userProfileClickListener = context
      } else {
         throw RuntimeException(context.toString())
      }
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      viewModel = ViewModelProvider(requireParentFragment())[TweetViewModel::class.java]
      super.onViewCreated(view, savedInstanceState)
      userId = requireContext().userId()
      viewModelObservable()
      binding()
      lifecycleScope.launch {
         delay(5000)
         viewModel.getFollowedUserIdList(userId)
         netWorkControlAndCloudRequestTweets()
      }
   }

   private fun viewModelObservable() {

      with(viewModel) {
         allRoomTweets.observe(viewLifecycleOwner) {
            if (it != null) {
               tweetAdapter.submitList(it)
               //    tweetsRoom = it
            }


         }
         mainStatus.observe(viewLifecycleOwner) {
            when (it!!) {
               TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()
               TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
               TweetRepository.MainStatus.DONE -> binding.lottiAnim.gone()
            }
         }

         mutableFollowNewTweet.observe(viewLifecycleOwner) {
            //SignalR dinliyor ve 2'den fazla tweet atılırsa clouddan tweetleri çekip tweetObserveden devam ediyor
            Log.e("try", it.size.toString())

            if (it!!.size >= 1) {
               viewModel.getTweets(userId)
               viewModel.mutableFollowNewTweet.value!!.clear()
               snackBar(requireView(), "Yeni Tweet Paylaşıldı", 2000)
            }
         }
      }
      newTweetButton()

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
            viewModel.getTweets(userId)
         }
      } else {
         viewModel.getTweets(userId)
      }
   }

   private fun newTweetButton() {
      with(binding) {
         with(viewModel) {

            tweets.observe(viewLifecycleOwner) { tweet ->
               seeNewTweet.setOnClickListener {
                  seeNewTweet.gone()
                  userPhoto1.gone()
                  userPhoto2.gone()
                  userPhoto3.gone()
                  tweetsRoomConvertAndAdd(tweet!!)
               }
            }
            mutableFollowNewTweetHashMap.observe(viewLifecycleOwner) {
               if (it.size == 1) {
                  Picasso.get().load(it.values.toTypedArray()[0]).into(userPhoto1)
                  seeNewTweet.visible()
                  userPhoto1.visible()
               } else if (it.size == 2) {
                  seeNewTweet.visible()
                  Picasso.get().load(it.values.toTypedArray()[1]).into(userPhoto2)
                  userPhoto2.visible()
               } else if (it.size >= 3) {
                  seeNewTweet.visible()
                  Picasso.get().load(it.values.toTypedArray()[0]).into(userPhoto1)
                  userPhoto1.visible()
                  Picasso.get().load(it.values.toTypedArray()[1]).into(userPhoto2)
                  userPhoto2.visible()
                  Picasso.get().load(it.values.toTypedArray()[2]).into(userPhoto3)
                  userPhoto3.visible()
               }
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



