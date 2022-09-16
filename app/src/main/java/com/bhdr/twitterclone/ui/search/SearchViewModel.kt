package com.bhdr.twitterclone.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepositoryImpl: SearchRepository) :
   ViewModel() {

   var statusM = MutableLiveData<Status>()
   val status: LiveData<Status>
      get() = statusM

   var followUserM = MutableLiveData<List<Users>>()
   val followUser: LiveData<List<Users>>
      get() = followUserM

   var followedUserM = MutableLiveData<Boolean>()
   val followedUser: LiveData<Boolean>
      get() = followedUserM

   var tagsM = MutableLiveData<List<String>>()
   val tags: LiveData<List<String>>
      get() = tagsM

   private var searchUserM = MutableLiveData<List<Users>>()
   val searchUser: LiveData<List<Users>> = searchUserM



   fun getSearchNotFollowUser(id: Int) {
      viewModelScope.launch {
         statusM.value = Status.LOADING
         followUserM.value = searchRepositoryImpl.getSearchFollowUser(id)
         statusM.value = Status.DONE

      }
   }

   fun getSearchFollowUser(userId: Int, followId: Int) {
      viewModelScope.launch {
         statusM.value = Status.LOADING
         followedUserM.value = searchRepositoryImpl.postUserFollow(userId, followId)
         statusM.value = Status.DONE
      }
   }

    fun getTags() {
      viewModelScope.launch {
         statusM.value = Status.LOADING
         tagsM.value = searchRepositoryImpl.getTags()
         statusM.value = Status.DONE
      }
   }

   fun searchUser(userName: String) {
      viewModelScope.launch {
         searchUserM.value = searchRepositoryImpl.searchUser(userName)
      }
   }
}