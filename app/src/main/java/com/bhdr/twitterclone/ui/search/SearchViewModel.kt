package com.bhdr.twitterclone.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.data.repos.SearchRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepositoryImpl: SearchRepositoryImpl) :
   ViewModel() {

   val status: LiveData<Status>
      get() = searchRepositoryImpl.searchStatus
   val followUser: LiveData<List<Users>>
      get() = searchRepositoryImpl.followUser

   val followedUser: LiveData<Boolean>
      get() = searchRepositoryImpl.followedUser

   val tags: LiveData<List<String>>
      get() = searchRepositoryImpl.tags


   init {
      getTags()
   }

   fun getSearchNotFollowUser(id: Int) {
      viewModelScope.launch {
         searchRepositoryImpl.getSearchFollowUser(id)
      }
   }

   fun getSearchFollowUser(userId: Int, followId: Int) {
      viewModelScope.launch {
         searchRepositoryImpl.postUserFollow(userId, followId)
      }
   }

   private fun getTags() {
      viewModelScope.launch {
         searchRepositoryImpl.getTags()
      }
   }

}