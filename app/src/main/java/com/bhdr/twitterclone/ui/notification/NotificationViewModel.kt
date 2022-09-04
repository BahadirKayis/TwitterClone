package com.bhdr.twitterclone.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.domain.repository.SearchRepository
import com.bhdr.twitterclone.domain.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
   private val tweetRepositoryImpl: TweetRepository,
   private val searchRepositoryImpl: SearchRepository
) :
   ViewModel() {

   var mutableNotificationList = MutableLiveData<List<Any>>()
   val notificationList: LiveData<List<Any>> = mutableNotificationList


   var followedCountM = MutableLiveData<List<Int>>()
   val followedCount: LiveData<List<Int>>
      get() = followedCountM


   fun notificationList() {
      viewModelScope.launch {
         mutableNotificationList.value = tweetRepositoryImpl.notificationList()
      }
   }

   var followedUserM = MutableLiveData<Boolean>()
   val followedUser: LiveData<Boolean>
      get() = followedUserM

   fun getSearchFollowUser(userId: Int, followId: Int) {
      viewModelScope.launch {
         followedUserM.value= searchRepositoryImpl.postUserFollow(userId, followId)
      }
   }

   fun followUserList(userId: Int) {
      viewModelScope.launch {
         followedCountM.value = searchRepositoryImpl.followUserList(userId)
      }
   }


}
