package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.viewmodels.SignUpViewModel
import com.bhdr.twitterclone.viewmodels.UserNameEmailViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.*

class LoginRepository {
    private val _userSaved = MutableLiveData<Boolean>()

    ///
    private val _userNE = MutableLiveData<List<UsernameAndEmailControl?>>()

    init {
        _userNE.value = null;
    }

    private val db = Firebase.storage

    private val signUpModel = UserNameEmailViewModel()
    fun userCreate(
        userName: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        date: String?,
        imageName: String,
        selectedPicture: Uri?
    ) {

        //Kontrol işlemi yapılacak önce

        val reference = db.reference
        val imagesReference = reference.child("profilpictures").child(imageName)
        imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskComplet ->
            val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
            uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                val profilePictureUrl = uri.toString()
                signUpModel.viewModelScope.launch {
                    try {
                        _userSaved.value = CallApi.retrofitService.createUser(
                            userName,
                            password,
                            name,
                            email,
                            phone,
                            profilePictureUrl,
                            date
                        );
                    } catch (e: Exception) {
                        _userSaved.value = false;
                        Log.e("user.Create", e.toString())
                    }

                }
            }
        }

    }

    fun getUsernameAndEmail(): MutableLiveData<List<UsernameAndEmailControl?>> {
        signUpModel.viewModelScope.launch {
            try {
                _userNE.value = CallApi.retrofitService.getUsernameAndEmail();

            } catch (e: Exception) {
                _userNE.value = null;
            }
            Log.e("userNE.value", _userNE.value.toString())
        }

        return _userNE

    }
}