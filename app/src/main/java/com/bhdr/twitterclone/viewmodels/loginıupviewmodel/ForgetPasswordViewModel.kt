package com.bhdr.twitterclone.viewmodels.loginıupviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhdr.twitterclone.repos.LoginRepository

class ForgetPasswordViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    val userId : LiveData<Int> = loginrepo._userIdForget
    var userChangePassword=MutableLiveData<Boolean>()
    var executeStatus=MutableLiveData<LoginRepository.LogInUpStatus>()
    init {
        userChangePassword=loginrepo.userChangePassword
        executeStatus=loginrepo.userStatus
    }

    fun userForgetId(userName: String){
        loginrepo.userForgetId(userName)
    }
    fun userChangePassword(userId: Int, password: String){
        loginrepo.userChangePassword(userId,password)
    }
}