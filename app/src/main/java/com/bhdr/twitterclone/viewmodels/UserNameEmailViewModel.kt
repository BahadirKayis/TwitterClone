package com.bhdr.twitterclone.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.repos.LoginRepository
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.launch

class UserNameEmailViewModel: ViewModel() {
  // private var loginrepo = LoginRepository()
     var userData = MutableLiveData<List<UsernameAndEmailControl?>>()

    init {
     // userData=loginrepo.getUsernameAndEmail()
      users()
    }
fun users(){
    viewModelScope.launch {
        try {
            userData.value = CallApi.retrofitService.getUsernameAndEmail();

        } catch (e: Exception) {
            userData.value=null;
            Log.e("userNE.value", e.toString())
        }
        Log.e("userNE.value", userData.value.toString() )
    }
}

}