package com.bhdr.twitterclone.fragments.mainfragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding


import com.bhdr.twitterclone.fragments.loginupfragments.SignInForgetPasswordSecondFragmentArgs
import com.bhdr.twitterclone.helperclasses.gone

import com.bhdr.twitterclone.helperclasses.visible
import com.bhdr.twitterclone.models.FollowedId
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.viewmodels.loginupviewmodel.SignInViewModel
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
    TweetsAdapter.ClickedTweetListener {
    private val binding by viewBinding(FragmentMainScreenBinding::bind)


    private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()//
    private val postViewModel by lazy { TweetViewModel() }
    private val signInViewModel by lazy { SignInViewModel() }
    private var userModel: Users? = null

    private  val followList: FollowedId?=null
    private val tweetAdapter by lazy { TweetsAdapter(this) }
    lateinit var shared : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInViewModel.getUserSigIn("testname")
        shared = requireContext().getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)


        viewModelObservable()
        try {
            postViewModel.getPosts(shared.getInt("user_Id",0))

        } catch (e: Exception) {
            Log.e("MainScreenFragment", e.toString())
        }

        binding.addTweetFAB.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
        }
    }

    private fun viewModelObservable() {
        postViewModel.mainStatus.observe(viewLifecycleOwner) {
            Log.e("viewLifecycleOwner", it.toString())


            when (it!!) {

                TweetRepository.MainStatus.LOADING -> binding.lottiAnim.visible()

                TweetRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
                TweetRepository.MainStatus.DONE -> binding.lottiAnim.gone()
            }
        }

        postViewModel.sharedFlowPost.observe(viewLifecycleOwner) {
            tweetAdapter.submitList(it)

        }

        postViewModel.liked.observe(viewLifecycleOwner) {

        }

        binding.tweetsRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                binding.tweetsRecyclerView.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = tweetAdapter
        }
        //tweetSignalR()

    }

    override fun crfButtonsListener(
        commentrtfav: String,
        tweetId: Int,
        currentlyCRFNumber: Int
    ) {
        when (commentrtfav) {
            "fav" -> postViewModel.postLiked(tweetId, currentlyCRFNumber)
            // "comment" -> viewModel.commentTweet(tweetDocId, currentlyCRFNumber)
            // "rt" -> viewModel.rtTweet(tweetDocId, currentlyCRFNumber)

        }
    }

    private fun followedControl(id: Int, imageUrl: String) {
        val haveId=followList?.find { it==id }



            Log.e("TAG", "haveId$haveId" )


    }

    private fun tweetSignalR() {
        val hubConnection =
            HubConnectionBuilder.create("http://192.168.3.136:9009/newTweetHub").build()
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()
        }
        hubConnection.on("newTweet", { id, imageUrl ->
            followedControl(id, imageUrl)
        }, Int::class.java, String::class.java)

 Log.e("Hub", hubConnection.connectionState.toString())
    }

}