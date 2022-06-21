package com.bhdr.twitterclone.viewmodels.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.MainRepository
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private var mainrepo = MainRepository()
    val sharedFlowPost= mainrepo.sharedFlow
    val mainStatus: LiveData<MainRepository.MainStatus> = mainrepo.mainStatus

    fun getPosts(id: Int) {
        viewModelScope.launch {
            mainrepo.getPosts(id)
        }

    }


}