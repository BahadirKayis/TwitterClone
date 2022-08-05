package com.bhdr.twitterclone.viewmodels.loginupviewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.LoginUpRepository
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {
   private var loginrepo = LoginUpRepository()
   val userSaved: LiveData<Boolean> = loginrepo.userSaved
   val userSavedStatus: LiveData<LoginUpRepository.LogInUpStatus> = loginrepo.userStatus

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
         loginrepo.signUP(
            userName,
            password,
            name,
            email,
            phone,
            date,
            imageName,
            selectedPicture
         )
      }

   }
}
