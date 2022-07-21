package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*


class LoginRepository {

    enum class LogInUpStatus { LOADING, ERROR, DONE }

    private val db = Firebase.storage
    val userData = MutableLiveData<List<UsernameAndEmailControl>>()//UserNameEmailViewModel


    val userSaved = MutableLiveData<Boolean>()//SignUpViewModel


    val userStatus = MutableLiveData<LogInUpStatus>()//SignUpViewModel - ForgetPasswordViewModel


    val userModel = MutableLiveData<Users>()//SignInViewModel


    val userForgetId = MutableLiveData<Int>()


    val userChangePassword = MutableLiveData<Boolean>()

    val loginAuto = MutableLiveData<Boolean>()

    suspend fun userForgetId(userName: String) {
        userStatus.value = LogInUpStatus.LOADING

        val userForget = CallApi.retrofitServiceLogInUp.getForgetPassword(userName)

        if (userForget.isSuccessful) {
            userForgetId.value = userForget.body()
            userStatus.value = LogInUpStatus.DONE
        } else if (!userForget.isSuccessful) {
            userForgetId.value = 0
            userStatus.value = LogInUpStatus.ERROR
        }


    }

    suspend fun userChangePassword(userId: Int, password: String) {
        userStatus.value = LogInUpStatus.LOADING

        val response =
            CallApi.retrofitServiceLogInUp.getForgetChangePassword(userId, password)

        if (response.isSuccessful) {
            userChangePassword.value = response.body()
            userStatus.value = LogInUpStatus.DONE
        } else if (!response.isSuccessful) {
            userChangePassword.value = false
            userStatus.value = LogInUpStatus.ERROR
        }

    }


    suspend fun signIn(userName: String) {
        userStatus.value = LogInUpStatus.LOADING
        val signIn = CallApi.retrofitServiceLogInUp.signIn(userName)

        if (signIn.isSuccessful) {
            userModel.value = signIn.body()
            userStatus.value = LogInUpStatus.DONE
        } else if (!signIn.isSuccessful) {
            userModel.value = null
            userStatus.value = LogInUpStatus.ERROR

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
        userStatus.value = LogInUpStatus.LOADING
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

                        userStatus.value = LogInUpStatus.DONE
                        userSaved.value = signUp.body()
                        if (userSaved.value == false) {
                             deleteImage(profilePictureUrl)
                        }

                    } else if (!signUp.isSuccessful) {
                        userStatus.value = LogInUpStatus.ERROR
                        deleteImage(profilePictureUrl)
                        Log.e("TAG", signUp.message() )
                        Log.e("TAG", signUp.errorBody().toString() )

                    }
                }

            }
        }
    }

    private  fun deleteImage(filename: String){//apiden false döndüğünde firebase yüklenen fotoğrafı siliyorum
        try {
            val photoRef: StorageReference = db.getReferenceFromUrl(filename)
            photoRef.delete().addOnSuccessListener {
                // File deleted successfully
                Log.e("TAG", "Photo is delete")
            }.addOnFailureListener { // Uh-oh, an error occurred!

            }
        }
        catch (e: Exception) {
            userStatus.value = LogInUpStatus.ERROR
            Log.e("TAG", e.toString())

        }

    }
    //UserNameEmailViewModel
    suspend fun getUsersData() {

        val getUsersData = CallApi.retrofitServiceLogInUp.getUsernameAndEmail()

        if (getUsersData.isSuccessful) {
            userData.value = getUsersData.body()
            Log.e("userData", userData.value.toString())
        } else {
            userData.value = null
            userStatus.value = LogInUpStatus.ERROR
        }

    }

suspend fun getLoginUserNameAndPassword(userName:String, password:String){
    val response= CallApi.retrofitServiceLogInUp.getLoginUserNameAndPassword(userName,password)
    Log.e("TAG", "$userName,$password ", )
    if(response.isSuccessful){
        loginAuto.value = response.body()
    }

}
}

