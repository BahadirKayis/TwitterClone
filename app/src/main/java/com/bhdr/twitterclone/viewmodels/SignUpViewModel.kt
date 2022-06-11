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
    //private var loginrepo = LoginRepository()
    private val _userSaved = MutableLiveData<Boolean>()
    private val db = Firebase.storage

    fun SignUp(
        userName: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        date: String?,
        imageName: String,
        selectedPicture: Uri?
    ) {
        //loginrepo.userCreate(userName, password,name, email, phone, date,imageName, selectedPicture)

        try {
            val reference = db.reference
            val imagesReference = reference.child("profilpictures").child(imageName)
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskComplet ->
                val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val profilePictureUrl = uri.toString()
                    Log.e("profilePictureUrl", profilePictureUrl.toString() )
                    viewModelScope.launch {
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
                            Log.e("ViewModelScope", e.toString())
                        }

                    }
                }
            }
        } catch (e: FirebaseException) {
            Log.e("Firebase", e.toString()+"" )
        }
    }

}
