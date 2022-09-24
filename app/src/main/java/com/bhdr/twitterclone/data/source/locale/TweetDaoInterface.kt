package com.bhdr.twitterclone.data.source.locale

import androidx.room.*
import com.bhdr.twitterclone.data.model.locale.NotificationsDataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel

@Dao
interface TweetDaoInterface {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)

   @Query("UPDATE tweets SET postLike=:postLikeCount Where id=:tweetId")
   suspend fun updateTweet(tweetId: Int, postLikeCount: Int)

   @Query("UPDATE tweets SET postLike=:postLikeCount , is_liked=:liked WHERE id=:tweetId")
   suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean)


   @Query("DELETE FROM tweets")
   suspend fun deleteTweet(): Int?

   @Query("SELECT * FROM tweets ORDER BY date DESC")
   suspend fun allTweet(): List<TweetsRoomModel>?

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNotificationTweet(tweet: NotificationsDataItem.NotificationTweet)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNotificationLike(tweet: NotificationsDataItem.NotificationLike)

   @Query("SELECT * FROM notificationTweet ORDER BY date DESC")
   suspend fun notificationListTweet(): List<NotificationsDataItem.NotificationTweet>

   @Query("SELECT * FROM notificationLike ORDER BY date DESC")
   suspend fun notificationListLike(): List<NotificationsDataItem.NotificationLike>

   @Query("DELETE  FROM notificationLike")
   suspend fun notificationDeleteLike()

   @Query("DELETE  FROM notificationTweet")
   suspend fun notificationDeleteTweet()

   @Query("SELECT * FROM notificationLike WHERE id=:tweetId")
   suspend fun isTweetQuery(tweetId: Int): List<NotificationsDataItem.NotificationLike>?

}