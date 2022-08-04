package com.bhdr.twitterclone.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TweetDaoInterface {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTweet(tweet: List<TweetsRoomModel>)

   @Update
   suspend  fun updateTweet(tweet: TweetsRoomModel)

   @Query("DELETE FROM tweets")
   suspend fun deleteTweet()

   @Query("SELECT * FROM tweets")
   fun allTweet(): LiveData<List<TweetsRoomModel?>>

}