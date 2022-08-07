package com.bhdr.twitterclone.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.bhdr.twitterclone.room.TweetsRoomModel

class TweetsCallBack : DiffUtil.ItemCallback<TweetsRoomModel>() {
   override fun areItemsTheSame(oldItem: TweetsRoomModel, newItem: TweetsRoomModel): Boolean {
      return oldItem.id == newItem.id
   }

   override fun areContentsTheSame(oldItem: TweetsRoomModel, newItem: TweetsRoomModel): Boolean {
      return oldItem == newItem
   }


}
