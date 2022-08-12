package com.bhdr.twitterclone.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TweetDaoInterface {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)

   @Query("UPDATE tweets SET postLike=:postLikeCount Where id=:tweetId")
   suspend fun updateTweet(tweetId: Int, postLikeCount: Int)

   @Query("UPDATE tweets SET postLike=:postLikeCount - is_liked=:liked WHERE id=:tweetId")
   suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean)

   @Query("DELETE FROM tweets")
   suspend fun deleteTweet()

   @Query("SELECT * FROM tweets ORDER BY date DESC")
   suspend fun allTweet(): List<TweetsRoomModel>?
}