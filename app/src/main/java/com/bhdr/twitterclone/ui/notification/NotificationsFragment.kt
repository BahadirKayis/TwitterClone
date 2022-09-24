package com.bhdr.twitterclone.ui.notification

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.databinding.FragmentNotificationsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(R.layout.fragment_notifications),
   NotificationsAdapter.ClickedUserFollow {

   private val viewModel: NotificationViewModel by viewModels()
   private val binding by viewBinding(FragmentNotificationsBinding::bind)
   private val notificationAdapter by lazy { NotificationsAdapter(this) }
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

      binding()
      networkControlRequest()
      observable()
   }

   private fun networkControlRequest() {
      if (requireContext().checkNetworkConnection()) {
         viewModel.followUserList(requireContext().userId())
      }

   }

   private fun observable() {
      with(viewModel) {
         notificationList()

         notificationList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
               snackBar(requireView(), "Bildiriminiz yok ", 1000)
            }
            notificationAdapter.setUserFollowItemsUpdate(it)
         }


         followedCount.observe(viewLifecycleOwner) {

            notificationAdapter.setUserFollowUpdate(it)
         }

         followedUser.observe(viewLifecycleOwner) {
            when (it) {
               true -> {
                  followUserList(requireContext().userId())
                  snackBar(requireView(), "Takip Edildi", 1000)
                  followedUser.removeObservers(requireParentFragment())
               }
               false -> {
                  snackBar(requireView(), "Bir hata oluştu lütfen tekrar deneyiniz", 1000)
               }
            }
         }

      }


   }

   private fun binding() {
      with(binding) {

         profilePicture.picasso(requireContext().userPhotoUrl())
         profilePicture.setOnClickListener {
            userProfileClickListener?.openDrawerClick()
         }
         binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
               this.context,
               LinearLayoutManager.VERTICAL,
               false
            )
            adapter = notificationAdapter

         }
      }

   }

   override fun followButtonsListener(followId: Int) {
      viewModel.getSearchFollowUser(requireContext().userId(), followId)
   }

}