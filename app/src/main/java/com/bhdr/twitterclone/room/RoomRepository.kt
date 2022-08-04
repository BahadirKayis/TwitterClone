package com.bhdr.twitterclone.room

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class RoomRepository(private val tweetDao: TweetDaoInterface) {
   val tweetsRoom: LiveData<List<TweetsRoomModel?>> = tweetDao.allTweet()
//   private val job = Job()
//   private val coroutineContext = Dispatchers.IO + job
//   private val caScope = CoroutineScope(coroutineContext)
   suspend fun tweetsInsert(tweets: List<TweetsRoomModel>) {
      try {
         tweetDao.addTweet(tweets)
      } catch (e: ClassCastException) {
         Log.e("class", e.toString())
      }
   }

   suspend fun tweetsUpdate(tweets: List<TweetsRoomModel>) {
      //tweetDao.updateTweet(tweets)
   }

   suspend fun tweetsDelete() {
      tweetDao.deleteTweet()
   }
}