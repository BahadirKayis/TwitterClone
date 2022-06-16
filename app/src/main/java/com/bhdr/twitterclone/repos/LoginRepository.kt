package com.bhdr.twitterclone.repos

import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.network.TwitterInterface
import com.bhdr.twitterclone.viewmodels.SignUpViewModel
import com.bhdr.twitterclone.viewmodels.UserNameEmailViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response

class LoginRepository  {

    enum class LogInUpStatus { LOADING, ERROR, DONE }
  val userSaved = MutableLiveData<Boolean>()
    val userSavedStatus=MutableLiveData<LogInUpStatus>()

    private val db = Firebase.storage

    val userData=MutableLiveData<List<UsernameAndEmailControl>>()

//SignUpViewModel
    fun SignUp(
        userName: String, password: String, name: String, email: String, phone: String, date: String?, imageName: String, selectedPicture: Uri?) {
            val reference = db.reference
            val imagesReference = reference.child("profilpictures").child(imageName)
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskComplet ->
                userSavedStatus.value=LogInUpStatus.LOADING
                val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val profilePictureUrl = uri.toString()
                    Log.e("profilePictureUrl", profilePictureUrl.toString())
                            CallApi.retrofitService.createUser( userName,
                                password,
                                name,
                                email,
                                phone,
                                profilePictureUrl,
                                date).enqueue(object :Callback<Boolean> {
                                override fun onResponse(
                                    call: Call<Boolean>,
                                    response: Response<Boolean>
                                ) {
                                    userSavedStatus.value=LogInUpStatus.DONE
                                    val tempList = response.body()
                                    userSaved.value = tempList
                                }

                                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                    Log.e("SignUpRepository", t.toString())
                                    userSavedStatus.value=LogInUpStatus.ERROR
                                    userSaved.value=false
                                }
                            })

                        }
                    }
    }

    //UserNameEmailViewModel
    fun getUsersData(){
        CallApi.retrofitService.getUsernameAndEmail().enqueue(object :Callback<List<UsernameAndEmailControl>>{
            override fun onResponse(
                call: Call<List<UsernameAndEmailControl>>,
                response: Response<List<UsernameAndEmailControl>>
            ) {
                userData.value=response.body()
                Log.e("userData", userData.value.toString())
            }

            override fun onFailure(call: Call<List<UsernameAndEmailControl>>, t: Throwable) {
             userData.value=null
                Log.e("userData", t.toString() )
              userSavedStatus.value=LogInUpStatus.ERROR
            }

        })

    }
}

