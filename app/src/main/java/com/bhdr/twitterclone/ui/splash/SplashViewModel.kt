package com.bhdr.twitterclone.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val loginRepo: LoginUpRepositoryImpl) : ViewModel(){

   val loginAuto: LiveData<Boolean> = loginRepo.loginAuto
   fun getLoginUserNameAndPassword(userName: String, password: String) {
      viewModelScope.launch {
         loginRepo.getLoginUserNameAndPassword(userName, password)
      }
}
}