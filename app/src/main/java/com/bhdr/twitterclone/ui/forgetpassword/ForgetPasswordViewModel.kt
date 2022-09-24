package com.bhdr.twitterclone.ui.forgetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(private val loginRepo: LoginUpRepository) :
   ViewModel() {

   private var userIdM = MutableLiveData<Int>()
   val userId: LiveData<Int> = userIdM


   fun userForgetId(userName: String) {

      viewModelScope.launch {
         userIdM.value = loginRepo.userForgetId(userName)

      }

   }
}