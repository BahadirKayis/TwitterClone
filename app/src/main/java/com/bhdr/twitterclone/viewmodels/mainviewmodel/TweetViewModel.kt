package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.repos.MainRepository
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.TweetsDatabase
import com.bhdr.twitterclone.room.TweetsRoomModel
import kotlinx.coroutines.launch

class TweetViewModel(application: Application) : AndroidViewModel(application) {
   //room
   private val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
   private val tweetRepository = TweetRepository(dao!!, application)

   val allRoomTweets: LiveData<List<TweetsRoomModel>?>
      get() = tweetRepository.tweetsRoomList


   val tweets: LiveData<List<Posts>?>//Insert işlemi için
      get() = tweetRepository.tweets

   val mainStatus: LiveData<TweetRepository.MainStatus>
      get() = tweetRepository.mainStatus

   init {
      viewModelScope.launch {
         tweetRepository.getTweetsRoom()
      }
   }

   private var mainRepository = MainRepository()

   val mutableFollowNewTweet: LiveData<HashMap<Int, String>>
      //Genel olarak dinliyor
      get() = mainRepository.mutableFollowNewTweet

   //BU da getTicketsa istek attığında yeni tweet varsa
   val mutableFollowNewTweetHashMap: LiveData<HashMap<Int, String>>
   get() = tweetRepository.mutableFollowNewTweetHashMap



   fun getTweets(id: Int) {
      viewModelScope.launch {
         tweetRepository.getTweets(id)
      }

   }

   fun postLiked(id: Int, count: Int, userId: Int) {
      viewModelScope.launch {
         tweetRepository.tweetLiked(id, count, userId)

      }
   }

   fun tweetsRoomConvertAndAdd(it: List<Posts>) {
      viewModelScope.launch {
         tweetRepository.tweetsRoomConvertAndAdd(it)
      }
   }



}
