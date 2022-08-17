package com.bhdr.twitterclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenLikeBinding
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenTweetBinding
import com.bhdr.twitterclone.helperclasses.DataItem
import com.bhdr.twitterclone.helperclasses.Database.TYPE_LIKE
import com.bhdr.twitterclone.helperclasses.Database.TYPE_TWEET

class NotificationsAdapter(private val notificationList: List<Any>) :
   RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   inner class LikeViewHolder(private val likeBinding: NotificationRecylerviewScreenLikeBinding) :
      RecyclerView.ViewHolder(likeBinding.root) {
      fun bind(item: DataItem.NotificationLike) {

      }

   }

   inner class TweetViewHolder(private val likeBinding: NotificationRecylerviewScreenTweetBinding) :
      RecyclerView.ViewHolder(likeBinding.root) {
      fun bind(item: DataItem.NotificationTweet) {

      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return when (viewType) {
         TYPE_LIKE -> LikeViewHolder(
            NotificationRecylerviewScreenLikeBinding.inflate(
               LayoutInflater.from(
                  parent.context
               ), parent, false
            )
         )
         TYPE_TWEET -> TweetViewHolder(
            NotificationRecylerviewScreenTweetBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
            )
         )
         else -> throw IllegalStateException("Unknown view type: $viewType")

      }
   }

   override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      when (holder) {
         is TweetViewHolder -> holder.bind(notificationList[position] as DataItem.NotificationTweet)
         is LikeViewHolder -> holder.bind(notificationList[position] as DataItem.NotificationLike)
      }
   }

   override fun getItemCount(): Int = notificationList.size
   override fun getItemViewType(position: Int): Int {
      return when (notificationList[position]) {
         is DataItem.NotificationTweet -> TYPE_TWEET
         is DataItem.NotificationLike -> TYPE_LIKE
         else -> throw IllegalStateException("Unknown view type: $notificationList")
      }
   }
}