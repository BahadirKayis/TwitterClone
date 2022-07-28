package com.bhdr.twitterclone.fragments.mainfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.viewmodels.mainviewmodel.TweetViewModel

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
   private val viewModel by lazy { TweetViewModel() }
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
   }


}