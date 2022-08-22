package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.adapters.NotificationsAdapter
import com.bhdr.twitterclone.databinding.FragmentNotificationsBinding
import com.bhdr.twitterclone.viewmodels.NotificationViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.sql.Timestamp
import java.time.LocalDateTime

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
   private lateinit var viewModel: NotificationViewModel
   private val binding by viewBinding(FragmentNotificationsBinding::bind)
   private val notificationAdapter by lazy { NotificationsAdapter() }
   private var notificationList: ArrayList<Any> = ArrayList()
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
            Log.e("TAG", it.toString())
            if (it != null) {

               notificationList.addAll(it)
               notificationAdapter.updateList(notificationList)
            }
         }

         notificationLike().observe(viewLifecycleOwner) {
            Log.e("TAGL", it.toString())
            if (it.isNotEmpty()) {
               notificationList.addAll(it)
               notificationAdapter.updateList(notificationList)
            }
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
}