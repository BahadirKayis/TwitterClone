package com.bhdr.twitterclone.viewmodels.loginıupviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.repos.LoginRepository
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    val userModel: LiveData<Users> = loginrepo.userModel

    fun getUserSigIn(userName: String) {

        loginrepo.signIn(userName)


    }
}