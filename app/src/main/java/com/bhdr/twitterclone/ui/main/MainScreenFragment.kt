package com.bhdr.twitterclone.ui.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
   TweetsAdapter.ClickedTweetListener {

   private val binding by viewBinding(FragmentMainScreenBinding::bind)
   private val viewModel: TweetViewModel by viewModels()
   private val tweetAdapter by lazy { TweetsAdapter(this) }
   private var userProfileClickListener: OpenMenu? = null

   override fun onAttach(context: Context) {
      super.onAttach(context)
      if (context is OpenMenu) {
         userProfileClickListener = context
      } else {
         throw RuntimeException(context.toString())
      }
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      // viewModel = ViewModelProvider(requireParentFragment())[TweetViewModel::class.java]
      super.onViewCreated(view, savedInstanceState)
      viewModelObservable()
      binding()
      remoteTweetRequest()
   }

   private fun remoteTweetRequest() {
      lifecycleScope.launch {
         delay(5000)
         netWorkControlAndCloudRequestTweets()
      }
   }

   private fun viewModelObservable() {

      with(viewModel) {
         allRoomTweets.observe(viewLifecycleOwner) {
            if (it != null) {
               tweetAdapter.submitList(it)
            } else {
               netWorkControlAndCloudRequestTweets()
            }
         }
         mainStatusL.observe(viewLifecycleOwner) {
            when (it!!) {
               Status.LOADING -> binding.lottiAnim.visible()
               Status.ERROR -> binding.lottiAnim.gone()
               Status.DONE -> binding.lottiAnim.gone()
            }
         }

         mutableFollowNewTweet.observe(viewLifecycleOwner) {
            //SignalR dinliyor ve 2'den fazla tweet atılırsa clouddan tweetleri çekip tweetObserveden devam ediyor
            Log.i("mutableFollowNewTweet", it.size.toString())

            //Test için 1 tweet
            if (it!!.size >= 1) {
               viewModel.getTweets(requireContext().userId())
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
      if (requireContext().checkNetworkConnection()) {
         viewModel.getFollowedUserIdList(requireContext().userId())
         viewModel.getTweets(requireContext().userId())
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
            mutableFollowNewTweetHashMapL.observe(viewLifecycleOwner) {
               if (it.size == 1) {
                  Picasso.get().load(it.values.toTypedArray()[0]).into(userPhoto1)
                  seeNewTweet.visible()
                  userPhoto1.visible()
                  updateIcon.visible()
               }

               if (it.size == 2) {

                  Picasso.get().load(it.values.toTypedArray()[0]).into(userPhoto1)
                  Picasso.get().load(it.values.toTypedArray()[1]).into(userPhoto2)

                  seeNewTweet.visible()
                  userPhoto1.visible()
                  userPhoto2.visible()
                  updateIcon.visible()

               } else if (it.size >= 3) {

                  Picasso.get().load(it.values.toTypedArray()[0]).into(userPhoto1)
                  Picasso.get().load(it.values.toTypedArray()[1]).into(userPhoto2)
                  Picasso.get().load(it.values.toTypedArray()[2]).into(userPhoto3)

                  userPhoto2.visible()
                  userPhoto1.visible()
                  userPhoto3.visible()
                  updateIcon.visible()
                  seeNewTweet.visible()
               }
            }
         }
      }
   }

   override fun crfButtonsListener(
      commentRtFav: String,
      tweetId: Int,
      currentlyCRFNumber: Int
   ) {
      when (commentRtFav) {
         "fav" -> viewModel.postLiked(tweetId, currentlyCRFNumber, requireContext().userId())
         // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
         // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)
      }
   }

   override fun onStop() {
      super.onStop()
      tweetAdapter.releasePlayer()
   }

}



