package com.bhdr.twitterclone.ui.signup

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private var loginRepo: LoginUpRepository) :
   ViewModel() {
   private var mss = MutableLiveData<Boolean>()
   val userSaved: LiveData<Boolean> = mss

   fun createUser(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
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
            imageName,
            selectedPicture
         ) {
            mss.value = it
         }
      }

   }

}
