package com.bhdr.twitterclone.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhdr.twitterclone.repos.LoginRepository

class ForgetPasswordViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    var userId = MutableLiveData<Int>()
    var userChangePassword=MutableLiveData<Boolean>()
    var executeStatus=MutableLiveData<LoginRepository.LogInUpStatus>()
    init {
        userId = loginrepo.userIdForget
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