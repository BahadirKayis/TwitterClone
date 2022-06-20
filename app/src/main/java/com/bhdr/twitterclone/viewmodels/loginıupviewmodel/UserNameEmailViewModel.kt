package com.bhdr.twitterclone.viewmodels.loginıupviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.repos.LoginRepository
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.launch
import kotlin.math.log

class UserNameEmailViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    var userData = MutableLiveData<List<UsernameAndEmailControl>>()


    init {

        getUsersData()
        userData = loginrepo.userData
    }

    fun getUsersData() {
        loginrepo.getUsersData()
    }
}