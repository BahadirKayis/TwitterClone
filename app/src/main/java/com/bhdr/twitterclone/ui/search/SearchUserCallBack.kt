package com.bhdr.twitterclone.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.bhdr.twitterclone.data.model.remote.Users

class SearchUserCallBack(
) : DiffUtil.ItemCallback<Users>() {
   override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
      TODO("Not yet implemented")
   }

   override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
      TODO("Not yet implemented")
   }

}