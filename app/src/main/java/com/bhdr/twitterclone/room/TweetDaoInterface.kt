package com.bhdr.twitterclone.room

import androidx.room.*
import com.bhdr.twitterclone.helperclasses.DataItem

@Dao
interface TweetDaoInterface {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)

   @Query("UPDATE tweets SET postLike=:postLikeCount Where id=:tweetId")
   suspend fun updateTweet(tweetId: Int, postLikeCount: Int)

   @Query("UPDATE tweets SET postLike=:postLikeCount , is_liked=:liked WHERE id=:tweetId")
   suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean)

   @Transaction
   @Query("DELETE FROM tweets")
   suspend fun deleteTweet(): Int?

   @Query("SELECT * FROM tweets ORDER BY date DESC")
   suspend fun allTweet(): List<TweetsRoomModel>?

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNotificationTweet(tweet: DataItem.NotificationTweet)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNotificationLike(tweet: DataItem.NotificationLike)

   @Query("SELECT * FROM notificationTweet ORDER BY date DESC")
   suspend fun notificationListTweet(): List<DataItem.NotificationTweet>

   @Query("SELECT * FROM notificationLike ORDER BY date DESC")
   suspend fun notificationListLike(): List<DataItem.NotificationLike>

   @Query("DELETE  FROM notificationLike")
   suspend fun notificationDeleteLike()

   @Query("DELETE  FROM notificationTweet")
   suspend fun notificationDeleteTweet()


}