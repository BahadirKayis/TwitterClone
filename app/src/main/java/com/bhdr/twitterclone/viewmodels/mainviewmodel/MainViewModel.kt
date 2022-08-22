package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.MainRepository
import com.bhdr.twitterclone.room.TweetsDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

   private val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
   private var mainRepository = MainRepository(dao!!)

   val notificationCount: LiveData<Int> =
      mainRepository.notificationCount

   val followCount: LiveData<Int>
      get() = mainRepository.followCount

   val followedCount: LiveData<Int>
      get() = mainRepository.followedCount

   fun followCount(userId: Int) {
      viewModelScope.launch {
         mainRepository.followCount(userId)
      }
   }

   fun followedCount(userId: Int) {
      viewModelScope.launch {
         mainRepository.followedCount(userId)
      }
   }

   fun startSignalR(userId: Int) {
      mainRepository.tweetSignalR(userId)
   }

   fun getFollowedUserIdList(id: Int) {
      viewModelScope.launch {
         mainRepository.getFollowedUserIdList(id)
      }
   }

   fun roomDelete() {
      viewModelScope.launch {
         mainRepository.deleteAllRoom()
      }
   }
}