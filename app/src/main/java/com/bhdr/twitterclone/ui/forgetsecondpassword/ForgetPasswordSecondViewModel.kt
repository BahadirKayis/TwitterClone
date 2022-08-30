package com.bhdr.twitterclone.ui.forgetsecondpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordSecondViewModel @Inject constructor(private val loginRepo: LoginUpRepositoryImpl) :
   ViewModel() {

   val userId: LiveData<Int> = loginRepo.userForgetId

   val userChangePassword: LiveData<Boolean> = loginRepo.userChangePassword


   var executeStatus: LiveData<Status> = loginRepo.userStatus


   fun userChangePassword(userId: Int, password: String) {
      viewModelScope.launch {
         loginRepo.userChangePassword(userId, password)
      }

   }
}