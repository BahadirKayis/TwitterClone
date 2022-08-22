package com.bhdr.twitterclone.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenLikeBinding
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenTweetBinding
import com.bhdr.twitterclone.helperclasses.DataItem
import com.bhdr.twitterclone.helperclasses.Database.TYPE_LIKE
import com.bhdr.twitterclone.helperclasses.Database.TYPE_TWEET
import com.bhdr.twitterclone.helperclasses.picasso
import com.bhdr.twitterclone.helperclasses.toCalendar

class NotificationsAdapter :
   RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   private var notificationList: List<Any> = listOf()


   inner class LikeViewHolder(private val likeBinding: NotificationRecylerviewScreenLikeBinding) :
      RecyclerView.ViewHolder(likeBinding.root) {

      fun bind(item: DataItem.NotificationLike) {
         with(likeBinding) {
            with(item) {
               profilePictureLiked.picasso(imageUrl.toString())
               nameTextLiked.text = name
               usernameTextLiked.text = "@$userName"
               timeTextLiked.text = "tweetini ${date!!.toLong().toCalendar()} beğendi"
               with(tweet) {
                  if (this!!.tweetImage != null) {
                     likeBinding.tweetImage.picasso(tweetImage!!)
                  }
                  tweetText.text = postContent

                  with(user!!) {
                     profilePicture.picasso(photo)
                     nameText.text = name
                     usernameText.text = "@$userName"
                     timeText.text = date!!.toLong().toCalendar()

                  }

               }
            }
         }
      }
   }

   inner class TweetViewHolder(private val tweetBinding: NotificationRecylerviewScreenTweetBinding) :
      RecyclerView.ViewHolder(tweetBinding.root) {
      fun bind(item: DataItem.NotificationTweet) {
         with(tweetBinding) {
            with(item) {
               profilePicture.picasso(imageUrl.toString())
               nameText.text = name
               usernameText.text = "@$userName"
               timeText.text = date!!.toLong().toCalendar()
               dateAndNewTweet.text = " adlı kullanıcı ${date!!.toLong().toCalendar()} tweetledi"
               with(tweet) {
                  tweetText.text = this!!.postContent
                  if (tweetImage != null) {
                     tweetBinding.tweetImage.picasso(tweetImage.toString())
                  }
               }
            }
         }
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

   fun updateList(notificationListFun: List<Any>) {
      notificationList = notificationListFun
      notifyDataSetChanged()
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
