package com.bhdr.twitterclone.ui.signup

import android.net.Uri
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
class SignUpViewModel @Inject constructor(private var loginRepo: LoginUpRepository) :
   ViewModel() {

 private  var userSavedM = MutableLiveData<Boolean>()
   val userSaved: LiveData<Boolean> = userSavedM


   var userSavedStatusM = MutableLiveData<Status>()
   val userSavedStatus: LiveData<Status> = userSavedStatusM

   fun createUser(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      date: String?,
      imageName: String,
      selectedPicture: Uri?
   ) {
      viewModelScope.launch {
         userSavedStatusM.value = Status.LOADING
         userSavedM.value = loginRepo.signUP(
            userName,
            password,
            name,
            email,
            phone,
            date,
            imageName,
            selectedPicture
         ).also {
            when (it) {
               true -> userSavedStatusM.value = Status.DONE
               false -> userSavedStatusM.value = Status.ERROR
            }
         }

      }


   }
}
