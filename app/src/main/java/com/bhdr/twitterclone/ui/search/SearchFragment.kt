package com.bhdr.twitterclone.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentSearchBinding
import com.bhdr.twitterclone.data.model.remote.Users
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), WhoToFollowAdapter.ClickedUserFollow {

   private val viewModel: SearchViewModel by viewModels()
   private val binding by viewBinding(FragmentSearchBinding::bind)
   lateinit var dataFollow: MutableList<Users>
   private lateinit var myAdapter: WhoToFollowAdapter

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      viewModel.getSearchNotFollowUser(requireContext().userId())
      binding()
      observable()

   }

   private fun observable() {
      with(viewModel) {

         status.observe(viewLifecycleOwner) {
            when (it!!) {
               Status.LOADING -> binding.lottiAnim.visible()
               Status.ERROR -> binding.lottiAnim.gone()
               Status.DONE -> binding.lottiAnim.gone()
            }
         }

         tags.observe(viewLifecycleOwner) {

            "Hello".also { binding.topContentContentText.text = it }
            binding.topContentSubjectText.text = it[0]

            val tags = TagsAdapter(it)
            binding.agendasRecyclerView.adapter = tags
         }

         followUser.observe(viewLifecycleOwner) {
            binding.whoToFollowRecyclerView.apply {
               dataFollow = it as MutableList<Users>

               myAdapter = WhoToFollowAdapter(
                  this@SearchFragment, dataFollow
               ) { index -> deleteItem(index) }

               adapter = myAdapter
            }
         }
      }
   }

   private fun binding() {
      with(binding) {
         agendasRecyclerView.apply {
            layoutManager =
               LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
         }
         whoToFollowRecyclerView.apply {
            layoutManager =
               LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
         }
         addTweetFAB.setOnClickListener {
            Navigation.findNavController(requireView())
               .navigate(R.id.action_searchFragment_to_addTweetFragment)
         }
         profilePicture.picasso(requireContext().userPhotoUrl())
      }
   }


   private fun deleteItem(index: Int) {
      if (::dataFollow.isInitialized && ::myAdapter.isInitialized) {

         if (!viewModel.followedUser.hasObservers()) {
            viewModel.followedUser.observe(viewLifecycleOwner) {
               when (it) {
                  true -> {
                     dataFollow.removeAt(index)
                     myAdapter.setItem(dataFollow, index)
                  }
                  false -> {
                     snackBar(requireView(), "Bir hata oluştu lütfen tekrar deneyiniz", 1000)
                  }
               }
            }
         }

      }
   }

   override fun followButtonsListener(followId: Int) {
      viewModel.getSearchFollowUser(requireContext().userId(), followId)
   }
}