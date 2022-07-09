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

    val followedUser: LiveData<Boolean>
        get() = searchRepository.followedUser

    val tags: LiveData<List<String>>
        get() = searchRepository.tags


    init {
         getTags()
    }

    fun getSearchFollowUser(id: Int) {
        viewModelScope.launch {
            searchRepository.getSearchFollowUser(id)
        }
    }
    fun getSearchFollowUser(userId:Int,followId:Int) {
        viewModelScope.launch {
            searchRepository.postUserFollow(userId,followId)
        }
    }

    private fun getTags() {
        viewModelScope.launch {
            searchRepository.getTags()
        }
    }

}