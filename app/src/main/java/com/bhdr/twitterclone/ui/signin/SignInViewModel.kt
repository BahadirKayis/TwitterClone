package com.bhdr.twitterclone.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val loginRepo: LoginUpRepository) : ViewModel() {

   private var userModelM = MutableLiveData<Users>()
   val userModel: LiveData<Users> = userModelM

   private var status = MutableLiveData<Status>()
   val statusL: LiveData<Status> = status

   fun getUserSigIn(userName: String) {
      viewModelScope.launch {
         status.value = Status.LOADING
         loginRepo.signIn(userName).let {
            userModelM.value = it
            status.value = Status.DONE
         }
      }
   }


}