package com.bhdr.twitterclone.ui.forgetsecondpassword

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
class ForgetPasswordSecondViewModel @Inject constructor(private val loginRepo: LoginUpRepository) :
   ViewModel() {


   private var userChangePasswordM = MutableLiveData<Boolean>()
   val userChangePassword: LiveData<Boolean> = userChangePasswordM


   fun userChangePassword(userId: Int, password: String) {
      viewModelScope.launch {
         userChangePasswordM.value = loginRepo.userChangePassword(userId, password)
      }

   }
}