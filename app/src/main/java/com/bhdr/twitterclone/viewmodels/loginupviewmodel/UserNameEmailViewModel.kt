package com.bhdr.twitterclone.viewmodels.loginupviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.repos.LoginRepository
import kotlinx.coroutines.launch

class UserNameEmailViewModel : ViewModel() {
    private var loginrepo = LoginRepository()
    var userData: LiveData<List<UsernameAndEmailControl>> = loginrepo.userData


    init {
        getUsersData()
    }

    private fun getUsersData() {
        viewModelScope.launch {
            loginrepo.getUsersData()
        }

    }
}