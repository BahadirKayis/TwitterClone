package com.bhdr.twitterclone.fragments.mainfragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding


import com.bhdr.twitterclone.helperclasses.gone
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.helperclasses.userPhotoUrl

import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.squareup.picasso.Picasso

import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
   TweetsAdapter.ClickedTweetListener {
   private val binding by viewBinding(FragmentMainScreenBinding::bind)
   private val viewModel by lazy { TweetViewModel() }
   private var followedUserIdList: List<Int>? = null
   private val tweetAdapter by lazy { TweetsAdapter(this) }

   var userId: Int? = null
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      userId =requireContext().userId()
      viewModel.getPosts(userId!!)
      viewModel.getFollowedUserIdList(userId!!)

      binding.addTweetFAB.setOnClickListener {
         Navigation.findNavController(requireView())
            .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
      }
      viewModelObservable()
      tweetSignalR()
Picasso.get().load(requireContext().userPhotoUrl()).into(binding.profilePicture)
   }

   private fun viewModelObservable() {
      viewModel.mainStatus.observe(viewLifecycleOwner) {
         when (it!!) {
            TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()

            TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
            TweetRepository.MainStatus.DONE -> binding.lottiAnim.gone()
         }
      }

      viewModel.sharedFlowPost.observe(viewLifecycleOwner) {
         tweetAdapter.submitList(it)

      }

      viewModel.liked.observe(viewLifecycleOwner) {

      }

      binding.tweetsRecyclerView.apply {
         layoutManager = LinearLayoutManager(
            binding.tweetsRecyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
         )
         adapter = tweetAdapter
      }

      viewModel.followedUserIdList.observe(viewLifecycleOwner) {
         Log.e("FollowedId", it.toString())
         followedUserIdList = it
      }
   }

   override fun crfButtonsListener(
      commentrtfav: String,
      tweetId: Int,
      currentlyCRFNumber: Int
   ) {
      when (commentrtfav) {
         "fav" -> viewModel.postLiked(tweetId, currentlyCRFNumber,requireContext())
         // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
         // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)

      }
   }

   private fun followedControl(id: Int, imageUrl: String) {
      val haveId = followedUserIdList?.find { it == id }
      haveId?.let {

      }
   }

   private fun tweetSignalR() {
      val hubConnection =
         HubConnectionBuilder.create("http://192.168.3.136:9009/newTweetHub").build()

      if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
         hubConnection.start()


      }
      hubConnection.on("newTweet", { id, imageUrl ->
         followedControl(id.toInt(), imageUrl)
      }, String::class.java, String::class.java)


   }

}