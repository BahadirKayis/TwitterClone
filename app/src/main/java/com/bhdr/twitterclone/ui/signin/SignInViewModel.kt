package com.bhdr.twitterclone.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val loginRepo: LoginUpRepository) : ViewModel() {

   var userModelM= MutableLiveData<Users>()
   val userModel: LiveData<Users> =userModelM


   fun getUserSigIn(userName: String) {
      viewModelScope.launch {
         userModelM.value=  loginRepo.signIn(userName)
      }
   }


}