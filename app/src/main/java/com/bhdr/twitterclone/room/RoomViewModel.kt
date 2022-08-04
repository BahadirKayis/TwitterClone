package com.bhdr.twitterclone.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.repos.TweetRepository
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {
   val allRoomTweets: LiveData<List<TweetsRoomModel?>>
   private  val repository: RoomRepository
   init {
      val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
      repository = RoomRepository(dao!!)
      allRoomTweets = repository.tweetsRoom
   }

   fun tweetInsert(tweets: List<TweetsRoomModel>) {
      try {

         viewModelScope.launch {

            repository.tweetsInsert(tweets)
         }
      } catch (e: ClassCastException) {
         Log.e("class", e.toString())
      }
   }

}