package com.bhdr.twitterclone.viewmodels.loginupviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.repos.LoginRepository
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    val userModel: LiveData<Users> = loginrepo.userModel

    companion object {
        lateinit var userModelCompanion : LiveData<Users>
    }

    init {
        userModelCompanion = loginrepo.userModel
    }


    fun getUserSigIn(userName: String) {
        viewModelScope.launch {
            loginrepo.signIn(userName)
        }


    }
}