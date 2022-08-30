package com.bhdr.twitterclone.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.data.repos.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepositoryImpl:MainRepositoryImpl) :ViewModel(){

   val notificationCount: LiveData<Int> =
      mainRepositoryImpl.notificationCount

   val followCount: LiveData<Int>
      get() = mainRepositoryImpl.followCount

   val followedCount: LiveData<Int>
      get() = mainRepositoryImpl.followedCount

   val roomDelete: LiveData<Boolean> get() = mainRepositoryImpl.roomDelete

   fun followCount(userId: Int) {
      viewModelScope.launch {
         mainRepositoryImpl.followCount(userId)
      }
   }

   fun followedCount(userId: Int) {
      viewModelScope.launch {
         mainRepositoryImpl.followedCount(userId)
      }
   }

   fun startSignalR(userId: Int) {
      mainRepositoryImpl.tweetSignalR(userId)
   }

   fun getFollowedUserIdList(id: Int) {
      viewModelScope.launch {
         mainRepositoryImpl.getFollowedUserIdList(id)
      }
   }

   fun roomDelete() {
      viewModelScope.launch {
         mainRepositoryImpl.deleteAllRoom()
      }
   }
}