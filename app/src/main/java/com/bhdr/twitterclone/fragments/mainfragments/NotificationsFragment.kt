package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.viewmodels.NotificationViewModel

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
   private lateinit var viewModel: NotificationViewModel
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      viewModel = ViewModelProvider(requireParentFragment())[NotificationViewModel::class.java]
      super.onViewCreated(view, savedInstanceState)
      observable()
   }

   private fun observable() {
      with(viewModel) {
         notificationLike()
         notificationTweet()
         notificationTweet().observe(viewLifecycleOwner) {

         }

         notificationLike().observe(viewLifecycleOwner) {

         }

      }
   }
}