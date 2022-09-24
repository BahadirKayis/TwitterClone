package com.bhdr.twitterclone.ui.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

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
      super.onViewCreated(view, savedInstanceState)
      viewModelObservable()
      binding()
      netWorkControlAndCloudRequestTweets()

   }

   private fun viewModelObservable() {

      with(viewModel) {
         allRoomTweets.observe(viewLifecycleOwner) {
            Log.i("allRoomTweetsObservable", it.toString())
            if (it?.size != 0) {
               tweetAdapter.submitList(it)

            }
         }
         mainStatusL.observe(viewLifecycleOwner) {
            when (it!!) {
               Status.LOADING -> binding.lottiAnim.visible()
               Status.ERROR -> binding.lottiAnim.gone()
               Status.DONE -> binding.lottiAnim.gone()
            }
         }
         followNewTweetListSignalR.observe(viewLifecycleOwner) {
            if (it!!.size >= 1) {
               getTweets(requireContext().userId())
               followNewTweetListSignalR.value!!.clear()
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
                  followNewTweetList.value!!.clear()
                  seeNewTweet.gone()
                  userPhoto1.gone()
                  userPhoto2.gone()
                  userPhoto3.gone()
                  updateIcon.gone()
                  tweetsRoomConvertAndAdd(tweet!!)
               }
            }
            followNewTweetList.observe(viewLifecycleOwner) {
               if (it!!.size == 1) {
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
         // "comment" ->
         // "rt" ->
      }
   }

   override fun onStop() {
      super.onStop()
      tweetAdapter.releasePlayer()
   }

}



