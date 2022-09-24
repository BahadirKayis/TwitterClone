package com.bhdr.twitterclone.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel

class TweetsCallBack : DiffUtil.ItemCallback<TweetsRoomModel>() {
   override fun areItemsTheSame(oldItem: TweetsRoomModel, newItem: TweetsRoomModel): Boolean {
      return oldItem.id == newItem.id
   }

   override fun areContentsTheSame(oldItem: TweetsRoomModel, newItem: TweetsRoomModel): Boolean {
      return oldItem == newItem
   }
}
