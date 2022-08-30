package com.bhdr.twitterclone.ui.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.common.gone
import com.bhdr.twitterclone.common.picasso
import com.bhdr.twitterclone.common.toCalendar
import com.bhdr.twitterclone.data.model.locale.DataItem
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenLikeBinding
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenTweetBinding


class NotificationsAdapter(
   private val clickedUserFollow: ClickedUserFollow
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   private var userFollow: List<Int> = emptyList()
   private var notificationList: List<Any> = emptyList()

   inner class LikeViewHolder(private val likeBinding: NotificationRecylerviewScreenLikeBinding) :
      RecyclerView.ViewHolder(likeBinding.root) {

      fun bind(item: DataItem.NotificationLike) {
         with(likeBinding) {
            with(item) {
               profilePictureLiked.picasso(imageUrl.toString())

               nameTextLiked.text = name

               "@$userName".also { usernameTextLiked.text = it }

               "tweetini ${date!!.toLong().toCalendar()} beğendi".also { timeTextLiked.text = it }

               with(tweet) {

                  if (this!!.tweetImage != null) {
                     likeBinding.tweetImage.picasso(tweetImage!!)

                  }

                  tweetText.text = postContent

                  with(user!!) {

                     profilePicture.picasso(photo)
                     nameText.text = name
                     "@$userName".also { usernameText.text = it }
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

               userFollow.find { it == id }.apply {
                  if (this != null) {
                     followUserButton.gone()
                  }
               }

               followUserButton.setOnClickListener {
                  clickedUserFollow.followButtonsListener(id!!)
               }

               profilePicture.picasso(imageUrl.toString())

               nameText.text = name

               "@$userName".also { usernameText.text = it }

               timeText.text = date!!.toLong().toCalendar()

               " adlı kullanıcı ${
                  date!!.toLong().toCalendar()
               } tweetledi".also { dateAndNewTweet.text = it }

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
         Database.TYPE_LIKE.viewType -> LikeViewHolder(
            NotificationRecylerviewScreenLikeBinding.inflate(
               LayoutInflater.from(
                  parent.context
               ), parent, false
            )
         )
         Database.TYPE_TWEET.viewType -> TweetViewHolder(
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
         is DataItem.NotificationTweet -> Database.TYPE_TWEET.viewType
         is DataItem.NotificationLike -> Database.TYPE_LIKE.viewType
         else -> throw IllegalStateException("Unknown view type: $notificationList")
      }
   }


   fun setUserFollow(newList: List<Int>) {
      try {
         val diffUtil = SetUserFollowCallBack(newList, userFollow)
         val diffResults = DiffUtil.calculateDiff(diffUtil)
         userFollow = newList
         diffResults.dispatchUpdatesTo(this)
      } catch (e: IndexOutOfBoundsException) {
         Log.e("setUserFollow-Ex", e.toString())
      }

   }

   fun setUserFollowItem(newList: List<Any>) {
      notificationList = newList
      notifyDataSetChanged()

   }

   interface ClickedUserFollow {
      fun followButtonsListener(followId: Int)
   }

   enum class Database(val viewType:Int) {
      TYPE_TWEET(0)
      , TYPE_LIKE(1) }
}
