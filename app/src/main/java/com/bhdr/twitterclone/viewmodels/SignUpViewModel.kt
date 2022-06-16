package com.bhdr.twitterclone.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.repos.LoginRepository
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.*


class SignUpViewModel : ViewModel() {
 private var loginrepo = LoginRepository()
     var userSaved = MutableLiveData<Boolean>()
     var userSavedStatus = MutableLiveData<LoginRepository.LogInUpStatus>()

init {

    userSaved = loginrepo.userSaved
    userSavedStatus= loginrepo.userSavedStatus
}
  fun SignUpview(userName: String,
                        password: String,
                        name: String,
                        email: String,
                        phone: String,
                        date: String?,
                        imageName: String,
                        selectedPicture: Uri?){
loginrepo.SignUp(userName, password, name, email, phone, date, imageName, selectedPicture)
}
}
