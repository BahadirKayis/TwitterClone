package com.bhdr.twitterclone.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SignInViewModel @Inject constructor(private val loginRepo: LoginUpRepositoryImpl) : ViewModel() {

   val userModel: LiveData<Users> = loginRepo.userModel


   fun getUserSigIn(userName: String) {
      viewModelScope.launch {
         loginRepo.signIn(userName)
      }
   }


}