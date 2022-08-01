package com.bhdr.twitterclone.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.bhdr.twitterclone.models.Users

class SearchUserCallBack : DiffUtil.ItemCallback<Users>() {
   override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
      return oldItem.id == newItem.id
   }

   override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
      return oldItem == newItem
   }
}