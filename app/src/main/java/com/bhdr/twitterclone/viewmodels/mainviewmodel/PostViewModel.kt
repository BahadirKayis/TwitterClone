package com.bhdr.twitterclone.viewmodels.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.repos.TweetRepository

import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private var mainrepo = TweetRepository()
    val sharedFlowPost:LiveData<List<Posts>> =mainrepo.sharedFlow
    val mainStatus: LiveData<TweetRepository.MainStatus> = mainrepo.mainStatus

    val liked: LiveData<Int> = mainrepo.liked
    fun getPosts(id: Int) {
        viewModelScope.launch {
            mainrepo.getPosts(id)
        }

    }

    fun postLiked(id: Int, count: Int) {
        viewModelScope.launch {
            mainrepo.postLiked(id, count)

        }
    }


}