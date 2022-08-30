package com.bhdr.twitterclone.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.data.repos.SearchRepositoryImpl
import com.bhdr.twitterclone.data.repos.TweetRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val tweetRepositoryImpl: TweetRepositoryImpl, private val searchRepositoryImpl: SearchRepositoryImpl) :
   ViewModel() {


   val notificationList: LiveData<List<Any>> = tweetRepositoryImpl.mutableNotificationList


   val followedCount: LiveData<List<Int>>
      get() = searchRepositoryImpl.followedCount

   fun notificationList() {
      viewModelScope.launch {
         tweetRepositoryImpl.notificationList()
      }
   }


   val followedUser: LiveData<Boolean>
      get() = searchRepositoryImpl.followedUser

   fun getSearchFollowUser(userId: Int, followId: Int) {
      viewModelScope.launch {
         searchRepositoryImpl.postUserFollow(userId, followId)
      }
   }

   fun followUserList(userId: Int) {
      viewModelScope.launch {
         searchRepositoryImpl.followUserList(userId)
      }
   }


}
