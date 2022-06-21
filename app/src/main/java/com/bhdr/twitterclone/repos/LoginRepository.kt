package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepository {

    enum class LogInUpStatus { LOADING, ERROR, DONE }

    private val db = Firebase.storage
    private val _userData = MutableLiveData<List<UsernameAndEmailControl>>()//UserNameEmailViewModel
    val userData: LiveData<List<UsernameAndEmailControl>>
        get() = _userData


    private val _userSaved = MutableLiveData<Boolean>()//SignUpViewModel
    val userSaved: LiveData<Boolean>
        get() = _userSaved


    private val _userStatus =
        MutableLiveData<LogInUpStatus>()//SignUpViewModel - ForgetPasswordViewModel
    val userStatus: LiveData<LogInUpStatus>
        get() = _userStatus

     val _userModel = MutableLiveData<Users>()//SignInViewModel
    val userModel: LiveData<Users>
        get() = _userModel

    private val _userForgetId = MutableLiveData<Int>()
    val userForgetId: LiveData<Int>
        get() = _userForgetId


    private val _userChangePassword = MutableLiveData<Boolean>()
    val userChangePassword: LiveData<Boolean>
        get() = _userChangePassword

    suspend fun userForgetId(userName: String) {
        _userStatus.value = LogInUpStatus.LOADING

        val userForget = CallApi.retrofitServiceLogInUp.getForgetPassword(userName)

        if (userForget.isSuccessful) {
            _userForgetId.value = userForget.body()
            _userStatus.value = LogInUpStatus.DONE
        } else if (!userForget.isSuccessful) {
            _userForgetId.value = 0
            _userStatus.value = LogInUpStatus.ERROR
        }


    }

    suspend fun userChangePassword(userId: Int, password: String) {
        _userStatus.value = LogInUpStatus.LOADING

        val userChangePassword =
            CallApi.retrofitServiceLogInUp.getForgetChangePassword(userId, password)

        if (userChangePassword.isSuccessful) {
            _userChangePassword.value = userChangePassword.body()
            _userStatus.value = LogInUpStatus.DONE
        } else if (!userChangePassword.isSuccessful) {
            _userChangePassword.value = false
            _userStatus.value = LogInUpStatus.ERROR
        }

    }


    suspend fun signIn(userName: String) {
        _userStatus.value = LogInUpStatus.LOADING
        val signIn = CallApi.retrofitServiceLogInUp.signIn(userName)

        if (signIn.isSuccessful) {
            _userModel.value = signIn.body()
            _userStatus.value = LogInUpStatus.DONE
        } else if (!signIn.isSuccessful) {
            _userModel.value = null
            _userStatus.value = LogInUpStatus.ERROR

        }


    }


//SignUpViewModel

    suspend fun signUP(
        userName: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        date: String?,
        imageName: String,
        selectedPicture: Uri?
    ) {
        _userStatus.value = LogInUpStatus.LOADING
        val reference = db.reference
        val imagesReference = reference.child("profilpictures").child(imageName)
        imagesReference.putFile(selectedPicture!!).addOnSuccessListener {

            val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
            uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                val profilePictureUrl = uri.toString()


                runBlocking {
                    val signUp = CallApi.retrofitServiceLogInUp.createUser(
                        userName,
                        password,
                        name,
                        email,
                        phone,
                        profilePictureUrl,
                        date
                    )

                    if (signUp.isSuccessful) {
                        _userStatus.value = LogInUpStatus.DONE

                        _userSaved.value = signUp.body()
                    } else if (!signUp.isSuccessful) {
                        _userStatus.value = LogInUpStatus.ERROR
                        _userSaved.value = false
                    }
                }

            }
        }
    }

    //UserNameEmailViewModel
    suspend fun getUsersData() {

        val getUsersData = CallApi.retrofitServiceLogInUp.getUsernameAndEmail()

        if (getUsersData.isSuccessful) {
            _userData.value = getUsersData.body()
            Log.e("userData", _userData.value.toString())
        } else {
            _userData.value = null
            _userStatus.value = LogInUpStatus.ERROR
        }

    }


}

