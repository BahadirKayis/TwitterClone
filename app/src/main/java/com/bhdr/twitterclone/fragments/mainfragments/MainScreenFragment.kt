package com.bhdr.twitterclone.fragments.mainfragments


import android.icu.number.NumberFormatter.with
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
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

   private val tweetAdapter by lazy { TweetsAdapter(this) }


   var userId: Int? = null
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      userId = requireContext().userId()
      viewModel.getPosts(userId!!)
      viewModel.getFollowedUserIdList(userId!!)
      viewModel.startSignalR()
      binding.addTweetFAB.setOnClickListener {
         Navigation.findNavController(requireView())
            .navigate(R.id.action_mainScreenFragment_to_addTweetFragment)
      }

      viewModelObservable()

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

      viewModel.listNewTweetImageUrl.observe(viewLifecycleOwner) {
         try {
            if (it.size == 2) {
               val url: String = it.values.toTypedArray()[0]
               Log.e("try", url)
               Picasso.get().load(url).into(binding.userPhoto1)
               Picasso.get().load(it.values.toTypedArray()[1]).into(binding.userPhoto2)
               binding.seeNewTweet.visible()
               binding.userPhoto1.visible()
               binding.userPhoto2.visible()
            } else if (it.size >= 3) {
               Picasso.get().load(it.values.toTypedArray()[2]).into(binding.userPhoto3)
               binding.userPhoto3.visible()
            }

         } catch (e: Exception) {
            Log.e("Exception", e.toString())
            e.printStackTrace()

         }


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

}




