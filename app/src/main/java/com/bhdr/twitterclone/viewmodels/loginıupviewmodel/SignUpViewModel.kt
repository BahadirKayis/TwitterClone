package com.bhdr.twitterclone.viewmodels.loginıupviewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bhdr.twitterclone.repos.LoginRepository


class SignUpViewModel : ViewModel() {
 private var loginrepo = LoginRepository()
     var userSaved = MutableLiveData<Boolean>()
     var userSavedStatus = MutableLiveData<LoginRepository.LogInUpStatus>()

init {

    userSaved = loginrepo.userSaved
    userSavedStatus= loginrepo.userStatus
}
  fun SignUpview(userName: String,
                        password: String,
                        name: String,
                        email: String,
                        phone: String,
                        date: String?,
                        imageName: String,
                        selectedPicture: Uri?){
loginrepo.signUP(userName, password, name, email, phone, date, imageName, selectedPicture)
}
}
