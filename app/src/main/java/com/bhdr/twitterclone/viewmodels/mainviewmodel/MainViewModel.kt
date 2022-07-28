package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.SignalRModel
import com.bhdr.twitterclone.repos.MainRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
   private var mainRepository = MainRepository()
   val mutableFollowNewTweet: LiveData<HashMap<Int, String>> = mainRepository.mutableFollowNewTweet
   val mutableNotFollowTweetOrLike: LiveData<List<SignalRModel>> =
      mainRepository.mutableNotFollowTweetOrLike



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

   fun startSignalR(context: Context) {
      mainRepository.tweetSignalR(context)
   }

   fun getFollowedUserIdList(id: Int) {
      viewModelScope.launch {
         mainRepository.getFollowedUserIdList(id)
      }
   }
}