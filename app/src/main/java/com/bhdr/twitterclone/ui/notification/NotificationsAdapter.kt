package com.bhdr.twitterclone.ui.notification

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bhdr.twitterclone.common.*
import com.bhdr.twitterclone.data.model.locale.NotificationsDataItem
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenLikeBinding
import com.bhdr.twitterclone.databinding.NotificationRecylerviewScreenTweetBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


class NotificationsAdapter(
   private val clickedUserFollow: ClickedUserFollow
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   private var userFollow: List<Int> = emptyList()
   private var notificationList: List<Any> = emptyList()
   private var context: Context? = null

   inner class LikeViewHolder(private val likeBinding: NotificationRecylerviewScreenLikeBinding) :
      RecyclerView.ViewHolder(likeBinding.root) {

      fun bind(item: NotificationsDataItem.NotificationLike) {
         with(likeBinding) {
            with(item) {
               profilePictureLiked.picasso(imageUrl.toString())

               nameTextLiked.text = name

               "@$userName".also { usernameTextLiked.text = it }

               "tweetini ${date!!.toLong().toCalendar()} beğendi".also { timeTextLiked.text = it }

               with(tweet) {

                  if (this!!.tweetImage != null) {
                     if (tweetImage!!.contains("video")) {
                        //Firebase boyutu bitmesin diye
                        //setup
//                        exoPlayer = ExoPlayer.Builder(likeBinding.root.context).build()
//                        exoPlayer?.playWhenReady = false
//                        playerView.player = exoPlayer
//                        //file
//                        val mediaItem =
//                           MediaItem.fromUri(tweetImage.toString())
//                        exoPlayer?.addMediaItem(mediaItem)
//
//                        exoPlayer?.playWhenReady = playWhenReady
//                        exoPlayer?.prepare()
                        playerView.visible()
                        likeBinding.tweetImage.gone()
                     } else {
                        playerView.gone()
                        likeBinding.tweetImage.visible()
                        likeBinding.tweetImage.picasso(tweetImage.toString())
                     }

                  }

                  tweetText.text = postContent
                  if (postContent?.contains("#") == true) {

                     tweetText.setSpannableFactory(spannableFactory())

                     tweetText.setText(
                        postContent.replace("-n", "\n"),
                        TextView.BufferType.SPANNABLE
                     )
                  } else {
                     tweetText.text = postContent!!.replace("-n", "\n")
                  }
                  with(user!!) {

                     profilePicture.picasso(photo)
                     nameText.text = name
                     "@$userName".also {
                        usernameText.text = if (it.length > 11) {
                           it.substring(0, 9) + "..."
                        } else {
                           it
                        }
                     }
                     timeText.text = date!!.toLong().toCalendar()

                  }

               }
            }
         }
      }
   }

   inner class TweetViewHolder(private val tweetBinding: NotificationRecylerviewScreenTweetBinding) :
      RecyclerView.ViewHolder(tweetBinding.root) {
      fun bind(item: NotificationsDataItem.NotificationTweet) {
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

               "@$userName".also {
                  usernameText.text = if (it.length > 11) {
                     it.substring(0, 9) + "..."
                  } else {
                     it
                  }
               }

               timeText.text = date!!.toLong().toCalendar()

               " adlı kullanıcı ${
                  date!!.toLong().toCalendar()
               } tweetledi".also { dateAndNewTweet.text = it }



               with(tweet) {

                  tweetText.text = this!!.postContent
                  if (postContent?.contains("#") == true) {

                     tweetText.setSpannableFactory(spannableFactory())

                     tweetText.setText(
                        postContent.replace("-n", "\n"),
                        TextView.BufferType.SPANNABLE
                     )
                  } else {
                     tweetText.text = postContent!!.replace("-n", "\n")
                  }
                  if (tweetImage != null) {
                     if (tweetImage!!.contains("video")) {
                        //Firebase boyutu bitmesin diye
                        //setup
//                        exoPlayer = ExoPlayer.Builder(tweetBinding.root.context!!).build()
//                        exoPlayer?.playWhenReady = false
//                        playerView.player = exoPlayer
//                        //file
//                        val mediaItem =
//                           MediaItem.fromUri(tweetImage.toString())
//                        exoPlayer?.addMediaItem(mediaItem)
//
//                        exoPlayer?.playWhenReady = playWhenReady
//                        exoPlayer?.prepare()
                        playerView.visible()
                        tweetBinding.tweetImage.gone()
                     } else {
                        playerView.gone()
                        tweetBinding.tweetImage.visible()
                        tweetBinding.tweetImage.picasso(tweetImage.toString())
                     }

                  }
               }
            }
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      //   context = parent.context
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

   private var exoPlayer: ExoPlayer? = null
   private var playbackPosition = 0L
   private var playWhenReady = false
   override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      when (holder) {
         is TweetViewHolder -> holder.bind(notificationList[position] as NotificationsDataItem.NotificationTweet)
         is LikeViewHolder -> holder.bind(notificationList[position] as NotificationsDataItem.NotificationLike)
      }
   }

   override fun getItemCount(): Int = notificationList.size

   override fun getItemViewType(position: Int): Int {
      return when (notificationList[position]) {
         is NotificationsDataItem.NotificationTweet -> Database.TYPE_TWEET.viewType
         is NotificationsDataItem.NotificationLike -> Database.TYPE_LIKE.viewType
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

   enum class Database(val viewType: Int) {
      TYPE_TWEET(0), TYPE_LIKE(1)
   }
}
