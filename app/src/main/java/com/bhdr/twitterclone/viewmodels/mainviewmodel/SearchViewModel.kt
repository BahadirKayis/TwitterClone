package com.bhdr.twitterclone.viewmodels.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Tags
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.SearchRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var searchRepository = SearchRepository()

    val status: LiveData<SearchRepository.MainStatus> = searchRepository.searchStatus
    val followUser: LiveData<List<Users>>
    get() = searchRepository.followUser

    val tags: LiveData<List<Tags>>
        get() = searchRepository.tags


    init {
    //    getTags()
    }
    fun getSearchFollowUser(id: Int) {
        viewModelScope.launch {
            searchRepository.getSearchFollowUser(id)
        }
    }
   private fun getTags(){
        viewModelScope.launch {
            searchRepository.getTags()
        }
    }
}