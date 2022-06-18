package com.bhdr.twitterclone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.LoginRepository

class SignInViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    var userModel = MutableLiveData<Users>()

    init {
        userModel = loginrepo.userModel
    }

    fun getUserSigIn(userName: String) {
        loginrepo.signIn(userName)
    }
}