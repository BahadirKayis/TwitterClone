package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    enum class LogInUpStatus { LOADING, ERROR, DONE }

    private val db = Firebase.storage
    val userData = MutableLiveData<List<UsernameAndEmailControl>>()

    val userSaved = MutableLiveData<Boolean>()
    val userStatus = MutableLiveData<LogInUpStatus>()

    val userModel = MutableLiveData<Users>()

    val userIdForget = MutableLiveData<Int>()
    val userChangePassword = MutableLiveData<Boolean>()

    fun userForgetId(userName: String) {
        userStatus.value = LogInUpStatus.LOADING
        CallApi.retrofitService.getForgetPassword(userName).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                userIdForget.value = response.body()
                userStatus.value = LogInUpStatus.DONE
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                userIdForget.value = 0
                userStatus.value = LogInUpStatus.ERROR

            }
        })
    }

    fun userChangePassword(userId: Int, password: String) {
        userStatus.value = LogInUpStatus.LOADING
        CallApi.retrofitService.getForgetChangePassword(userId, password)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    userChangePassword.value = response.body()
                    userStatus.value = LogInUpStatus.DONE
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    userChangePassword.value = false
                    userStatus.value = LogInUpStatus.ERROR
                }
            })
    }

    fun signIn(userName: String) {
        CallApi.retrofitService.signIn(userName).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                userModel.value = response.body()
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("repLogSignIn", t.toString())
                userModel.value = null
            }
        })
    }

    //SignUpViewModel

    fun signUP(
        userName: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        date: String?,
        imageName: String,
        selectedPicture: Uri?
    ) {
        userStatus.value = LogInUpStatus.LOADING
        val reference = db.reference
        val imagesReference = reference.child("profilpictures").child(imageName)
        imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskComplet ->

            val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
            uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                val profilePictureUrl = uri.toString()

                CallApi.retrofitService.createUser(
                    userName,
                    password,
                    name,
                    email,
                    phone,
                    profilePictureUrl,
                    date
                ).enqueue(object : Callback<Boolean> {
                    override fun onResponse(
                        call: Call<Boolean>,
                        response: Response<Boolean>
                    ) {
                        userStatus.value = LogInUpStatus.DONE
                        val tempList = response.body()
                        userSaved.value = tempList
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Log.e("repLogSignUp", t.toString())
                        userStatus.value = LogInUpStatus.ERROR
                        userSaved.value = false
                    }
                })

            }
        }
    }

    //UserNameEmailViewModel
    fun getUsersData() {
        CallApi.retrofitService.getUsernameAndEmail()
            .enqueue(object : Callback<List<UsernameAndEmailControl>> {
                override fun onResponse(
                    call: Call<List<UsernameAndEmailControl>>,
                    response: Response<List<UsernameAndEmailControl>>
                ) {
                    userData.value = response.body()
                    Log.e("userData", userData.value.toString())
                }

                override fun onFailure(call: Call<List<UsernameAndEmailControl>>, t: Throwable) {
                    userData.value = null
                    Log.e("userData", t.toString())
                    userStatus.value = LogInUpStatus.ERROR
                }

            })

    }
}

