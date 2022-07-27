package com.bhdr.twitterclone.viewmodels.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.MainRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
   private var mainRepository = MainRepository()

   val followCount: LiveData<Int>
      get() = mainRepository.followCount

   val followedCount: LiveData<Int>
      get() = mainRepository.followedCount

   fun followCount(userId: Int){
      viewModelScope.launch {
         mainRepository.followCount(userId)
      }
   }
   fun followedCount(userId: Int){
      viewModelScope.launch {
      mainRepository.followedCount(userId)
   }
   }
}