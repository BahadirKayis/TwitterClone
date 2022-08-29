package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.NotificationsAdapter
import com.bhdr.twitterclone.databinding.FragmentNotificationsBinding
import com.bhdr.twitterclone.helperclasses.picasso
import com.bhdr.twitterclone.helperclasses.snackBar
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.helperclasses.userPhotoUrl
import com.bhdr.twitterclone.viewmodels.NotificationViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class NotificationsFragment : Fragment(R.layout.fragment_notifications),
   NotificationsAdapter.ClickedUserFollow {

   private lateinit var viewModel: NotificationViewModel
   private val binding by viewBinding(FragmentNotificationsBinding::bind)
   private val notificationAdapter by lazy { NotificationsAdapter(this) }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      viewModel = ViewModelProvider(requireParentFragment())[NotificationViewModel::class.java]
      super.onViewCreated(view, savedInstanceState)
      binding()
      observable()
   }

   private fun observable() {
      with(viewModel) {
         followUserList(requireContext().userId())
         notificationList()

         notificationList.observe(viewLifecycleOwner) {
            if (it == null) {
               snackBar(requireView(), "Bildiriminiz yok ", 1000)
            }
            notificationAdapter.setUserFollowItem(it)
         }


         followedCount.observe(viewLifecycleOwner) {

            notificationAdapter.setUserFollow(it)
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
         recyclerView.apply {
            layoutManager = LinearLayoutManager(
               this.context,
               LinearLayoutManager.VERTICAL,
               false
            )
            adapter = notificationAdapter

         }

         profilePicture.picasso(requireContext().userPhotoUrl())
      }

   }

   override fun followButtonsListener(followId: Int) {
      viewModel.getSearchFollowUser(requireContext().userId(), followId)
   }

}