package com.bhdr.twitterclone.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.bhdr.twitterclone.models.Posts

class TweetsCallBack : DiffUtil.ItemCallback<Posts>() {
   override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
      return oldItem.id == newItem.id
   }

   override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
      return oldItem == newItem
   }


}
