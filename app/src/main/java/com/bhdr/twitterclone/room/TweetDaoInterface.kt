package com.bhdr.twitterclone.room

import androidx.room.*

@Dao
interface TweetDaoInterface {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)

   @Update
   suspend fun updateTweet(tweet: TweetsRoomModel)

   @Query("DELETE FROM tweets")
   suspend fun deleteTweet()

   @Query("SELECT * FROM tweets")
   suspend fun allTweet(): List<TweetsRoomModel?>

}