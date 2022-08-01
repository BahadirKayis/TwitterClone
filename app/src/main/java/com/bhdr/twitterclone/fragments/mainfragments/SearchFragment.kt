package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TagsAdapter
import com.bhdr.twitterclone.adapters.WhoToFollowAdapter
import com.bhdr.twitterclone.databinding.FragmentSearchBinding
import com.bhdr.twitterclone.helperclasses.*
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.SearchRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.SearchViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SearchFragment : Fragment(R.layout.fragment_search), WhoToFollowAdapter.ClickedUserFollow {
   private val searchModel by lazy { SearchViewModel() }
   private val binding by viewBinding(FragmentSearchBinding::bind)
   lateinit var dataFollow: MutableList<Users>
   private lateinit var myAdapter: WhoToFollowAdapter

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      observeViewModel()
      searchModel.getSearchFollowUser(requireContext().userId())
      binding.apply {


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

   private fun observeViewModel() {
      searchModel.apply {

         status.observe(viewLifecycleOwner) {
            when (it!!) {
               SearchRepository.MainStatus.LOADING -> binding.lottiAnim.visible()
               SearchRepository.MainStatus.ERROR -> binding.lottiAnim.gone()
               SearchRepository.MainStatus.DONE -> binding.lottiAnim.gone()
            }
         }

         tags.observe(viewLifecycleOwner) {

            binding.topContentContentText.text = "Hello"
            binding.topContentSubjectText.text = it[0]

            val tags = TagsAdapter(it)
            binding.agendasRecyclerView.adapter = tags
         }

         followUser.observe(viewLifecycleOwner) {
            binding.whoToFollowRecyclerView.apply {
               dataFollow = it as MutableList<Users>
               myAdapter = WhoToFollowAdapter(
                  this@SearchFragment,
                  dataFollow
               ) { index -> deleteItem(index) }
               adapter = myAdapter
            }
         }
      }
   }

   private fun deleteItem(index: Int) {
      if (::dataFollow.isInitialized && ::myAdapter.isInitialized) {

         if (!searchModel.followedUser.hasObservers()) {
            searchModel.followedUser.observe(viewLifecycleOwner) {

               when (it) {
                  true -> {
                     dataFollow.removeAt(index)
                     myAdapter.setItem(dataFollow)
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
      searchModel.getSearchFollowUser(requireContext().userId(), followId)
   }
}