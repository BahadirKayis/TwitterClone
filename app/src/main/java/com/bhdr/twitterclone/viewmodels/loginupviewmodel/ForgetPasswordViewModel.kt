package com.bhdr.twitterclone.viewmodels.loginupviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.LoginRepository

import kotlinx.coroutines.launch

class ForgetPasswordViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    val userId: LiveData<Int> = loginrepo.userForgetId

    val userChangePassword: LiveData<Boolean> = loginrepo.userChangePassword


    var executeStatus: LiveData<LoginRepository.LogInUpStatus> = loginrepo.userStatus

    fun userForgetId(userName: String) {
        viewModelScope.launch {
            loginrepo.userForgetId(userName)
        }

    }

    fun userChangePassword(userId: Int, password: String) {
        viewModelScope.launch {
            loginrepo.userChangePassword(userId, password)
        }

    }
}