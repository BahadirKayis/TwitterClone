package com.bhdr.twitterclone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.common.toStartSignalRTweet
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.data.repos.TweetRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetViewModel @Inject constructor(private val tweetRepositoryImpl: TweetRepositoryImpl) :
   ViewModel() {
   //room
   val allRoomTweets: LiveData<List<TweetsRoomModel>?>
      get() = tweetRepositoryImpl.tweetsRoomList


   //Insert işlemi için
   val tweets: LiveData<List<Posts>?>
      get() = tweetRepositoryImpl.tweets

   val mainStatus: LiveData<Status>
      get() = tweetRepositoryImpl.mainStatus

   init {
      viewModelScope.launch {
         tweetRepositoryImpl.getTweetsRoom()
         if (!toStartSignalRTweet) {
            toStartSignalRTweet = true
            delay(10000)
            tweetRepositoryImpl.tweetSignalR()
         }

      }
   }

   val mutableFollowNewTweet: LiveData<HashMap<Int, String>> =
      tweetRepositoryImpl.mutableFollowNewTweet
   //Genel olarak dinliyor


   //BU da getTicketsa istek attığında yeni tweet varsa
   val mutableFollowNewTweetHashMap: LiveData<HashMap<Int, String>>
      get() = tweetRepositoryImpl.mutableFollowNewTweetHashMap


   fun getTweets(id: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.getTweets(id)
      }

   }

   fun postLiked(id: Int, count: Int, userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.tweetLiked(id, count, userId)

      }
   }

   fun tweetsRoomConvertAndAdd(it: List<Posts>) {
      viewModelScope.launch {
         tweetRepositoryImpl.tweetsRoomConvertAndAdd(it)
      }
   }

   fun getFollowedUserIdList(userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.getFollowedUserIdList(userId)
      }
   }


}
