package com.bhdr.twitterclone.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.databinding.FragmentSearchBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), WhoToFollowAdapter.ClickedUserFollow {

   private val viewModel: SearchViewModel by viewModels()
   private val binding by viewBinding(FragmentSearchBinding::bind)

   private lateinit var myAdapter: WhoToFollowAdapter
   private lateinit var searchUserAdapter: SearchUserAdapter
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
      searchUserAdapter = SearchUserAdapter(requireContext(), R.layout.autocompletetextview_search)
      networkControlRequest()
      binding()
      observable()

   }

   private fun networkControlRequest() {
      if (requireContext().checkNetworkConnection()) {
         viewModel.getTags()
         viewModel.getSearchNotFollowUser(requireContext().userId())
         binding.lottiAnimNetworkError.gone()
      } else {
         binding.lottiAnimNetworkError.visible()
      }

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

               myAdapter = WhoToFollowAdapter(
                  this@SearchFragment, it as MutableList<Users>
               )

               adapter = myAdapter
            }
         }
         searchUser.observe(viewLifecycleOwner) {
            if (it != null) {
               Log.i("searchUserResponse", it.toString())
               searchUserAdapter.userDataChanged(it)
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
         profilePicture.setOnClickListener {
            userProfileClickListener?.openDrawerClick()
         }

         autoCompleteTextView.setAdapter(searchUserAdapter)

         //Cloud Filter
         autoCompleteTextView.doOnTextChanged { text, _, _, _ ->
            if (requireContext().checkNetworkConnection()) {
               viewModel.searchUser(
                  text.toString()
               )
            }

         }
         //Local Filter
         // viewModel.searchUser("allUsers")

         searchUserAdapter.onClickItem = ::autoCompleteTextViewItemSelect


      }
   }

   private fun autoCompleteTextViewItemSelect(userName: String) {
      binding.autoCompleteTextView.setText(userName)
   }

   override fun followButtonsListener(followId: Int) {
      viewModel.getSearchFollowUser(requireContext().userId(), followId)
   }

}