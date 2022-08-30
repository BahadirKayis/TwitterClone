package com.bhdr.twitterclone.ui.signup

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private var loginRepo: LoginUpRepositoryImpl) : ViewModel() {

   val userSaved: LiveData<Boolean> = loginRepo.userSaved
   val userSavedStatus: LiveData<Status> = loginRepo.userStatus

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
         loginRepo.signUP(
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
