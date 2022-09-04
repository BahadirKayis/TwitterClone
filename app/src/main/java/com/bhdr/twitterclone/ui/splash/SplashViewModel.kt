package com.bhdr.twitterclone.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val loginRepo: LoginUpRepository) : ViewModel() {

   var loginAutoM = MutableLiveData<Boolean>()
   val loginAuto: LiveData<Boolean> = loginAutoM
   fun getLoginUserNameAndPassword(userName: String, password: String) {
      viewModelScope.launch {
         loginAutoM.value = loginRepo.getLoginUserNameAndPassword(userName, password)
      }
   }
}