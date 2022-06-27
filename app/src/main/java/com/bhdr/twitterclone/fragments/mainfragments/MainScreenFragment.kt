package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TweetsAdapter
import com.bhdr.twitterclone.databinding.FragmentMainScreenBinding

import com.bhdr.twitterclone.fragments.loginupfragments.SignInForgetPasswordSecondFragmentArgs
import com.bhdr.twitterclone.helperclasses.loadingDialogStart
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.PostViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainScreenFragment : Fragment(R.layout.fragment_main_screen),
    TweetsAdapter.ClickedTweetListener {
    private val binding by viewBinding(FragmentMainScreenBinding::bind)
    private val args: SignInForgetPasswordSecondFragmentArgs by navArgs()//
    private val postViewModel by lazy { PostViewModel() }
    private var userModel: Users? = null

   private val tweetAdapter by lazy { TweetsAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // userModel = SignInViewModel.userModelCompanion.value
        viewModelObservable()
        try {
            postViewModel.getPosts(1)

        } catch (e: Exception) {
            Log.e("MainScreenFragment", e.toString())
        }


    }

    private fun viewModelObservable() {

        postViewModel.mainStatus.observe(viewLifecycleOwner) {
            Log.e("viewLifecycleOwner", it.toString())
//            when (it!!) {
//
//                TweetRepository.MainStatus.LOADING -> loadingDialogStart(requireActivity()).show()
//
//                TweetRepository.MainStatus.ERROR -> loadingDialogStart(requireActivity()).dismiss()
//                TweetRepository.MainStatus.DONE -> loadingDialogStart(requireActivity()).dismiss()
//            }
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

}