package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.app.Application
import android.util.Log
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
   val allRoomTweets: LiveData<List<TweetsRoomModel?>?>


   private val tweetRepository: TweetRepository

   val tweets: LiveData<List<Posts>>
      get() = tweetRepository.tweets

   val mainStatus: LiveData<TweetRepository.MainStatus>
      get() = tweetRepository.mainStatus

   init {
      val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
      tweetRepository = TweetRepository(dao!!)
      allRoomTweets = tweetRepository.tweetsRoomList
      Log.e("TAG", "init viewModle ")
      viewModelScope.launch {
         tweetRepository.tweetsRoomModelConvertPostModel()
      }
   }

   private var mainRepository = MainRepository()

   val mutableFollowNewTweet: LiveData<HashMap<Int, String>>
      get() = mainRepository.mutableFollowNewTweet


   fun getTweets(id: Int) {
      viewModelScope.launch {
         tweetRepository.getTweets(id)
      }

   }

   fun postLiked(id: Int, count: Int, userId: Int) {
      viewModelScope.launch {
         tweetRepository.postLiked(id, count, userId)

      }
   }


   fun tweetsRoomConvertAndAdd(it: List<Posts>) {
      viewModelScope.launch {
         tweetRepository.tweetsRoomConvertAndAdd(it)
      }

   }
}
