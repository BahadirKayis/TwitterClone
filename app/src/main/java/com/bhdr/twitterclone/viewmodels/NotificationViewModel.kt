package com.bhdr.twitterclone.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.SearchRepository
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.TweetsDatabase
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
   private val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
   private val tweetRepository = TweetRepository(dao!!, application)


   val notificationList: LiveData<List<Any>> = tweetRepository.mutableNotificationList


   val followedCount: LiveData<List<Int>>
      get() = searchRepository.followedCount

   fun notificationList() {
      viewModelScope.launch {
         tweetRepository.notificationList()
      }
   }

   private var searchRepository = SearchRepository()
   val followedUser: LiveData<Boolean>
      get() = searchRepository.followedUser

   fun getSearchFollowUser(userId: Int, followId: Int) {
      viewModelScope.launch {
         searchRepository.postUserFollow(userId, followId)
      }
   }

   fun followUserList(userId: Int) {
      viewModelScope.launch {
         searchRepository.followUserList(userId)
      }
   }


}
