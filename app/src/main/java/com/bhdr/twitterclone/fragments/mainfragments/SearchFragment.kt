package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.TagsAdapter
import com.bhdr.twitterclone.adapters.WhoToFollowAdapter
import com.bhdr.twitterclone.databinding.FragmentSearchBinding
import com.bhdr.twitterclone.helperclasses.loadingDialogStart
import com.bhdr.twitterclone.repos.SearchRepository
import com.bhdr.twitterclone.viewmodels.mainviewmodel.SearchViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SearchFragment : Fragment(R.layout.fragment_search),WhoToFollowAdapter.ClickedUserFollow {
    private val searchModel by lazy { SearchViewModel() }
    private val binding by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        searchModel.getSearchFollowUser(1)

        binding.agendasRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
        }
        binding.whoToFollowRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModel() {

        searchModel.status.observe(viewLifecycleOwner) {
            when (it!!) {
                SearchRepository.MainStatus.LOADING -> loadingDialogStart(requireActivity())
                SearchRepository.MainStatus.ERROR -> loadingDialogStart(requireActivity()).dismiss()
                SearchRepository.MainStatus.DONE -> loadingDialogStart(requireActivity()).dismiss()
            }
        }
        searchModel.tags.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString() )
            binding.topContentContentText.text = "Naruto"
            binding.topContentSubjectText.text = it[0].tagName.also {
                it?.drop(0)
                Log.e("TAG", it.toString() )
            }
            val tags = TagsAdapter(it)
            binding.agendasRecyclerView.adapter = tags
        }

        searchModel.followUser.observe(viewLifecycleOwner) {

            val users = WhoToFollowAdapter(this,it)
            binding.whoToFollowRecyclerView.adapter = users

        }

        searchModel.followedUser.observe(viewLifecycleOwner) {
            Log.e("TAG", it.toString())
        }

    }

    override fun followButtonsListener(followId: Int) {
        searchModel.getSearchFollowUser(1,followId)
    }
}