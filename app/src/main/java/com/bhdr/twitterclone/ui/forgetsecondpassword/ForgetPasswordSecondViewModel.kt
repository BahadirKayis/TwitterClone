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

   var status = MutableLiveData<Status>()
   val executeStatus: LiveData<Status> = status


   fun userChangePassword(userId: Int, password: String) {
      viewModelScope.launch {
         status.value = Status.LOADING
         userChangePasswordM.value = loginRepo.userChangePassword(userId, password)
         status.value = Status.DONE
      }

   }
}