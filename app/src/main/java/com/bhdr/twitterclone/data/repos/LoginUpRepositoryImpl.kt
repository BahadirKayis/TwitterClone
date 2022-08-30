package com.bhdr.twitterclone.data.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking


class LoginUpRepositoryImpl constructor(private val remoteSource: RemoteDataSourceLogin) {

//   enum class LogInUpStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val userSaved = MutableLiveData<Boolean>()//SignUpViewModel

   val userStatus = MutableLiveData<Status>()//SignUpViewModel - ForgetPasswordViewModel

   val userModel = MutableLiveData<Users>()//SignInViewModel

   val userForgetId = MutableLiveData<Int>()

   val userChangePassword = MutableLiveData<Boolean>()

   val loginAuto = MutableLiveData<Boolean>()

   suspend fun userForgetId(userName: String) {
      userStatus.value = Status.LOADING

      val userForget = remoteSource.getForgetPassword(userName)

      if (userForget.isSuccessful) {
         userForgetId.value = userForget.body()
         userStatus.value = Status.DONE
      } else if (!userForget.isSuccessful) {
         userForgetId.value = 0
         userStatus.value = Status.ERROR
      }


   }

   suspend fun userChangePassword(userId: Int, password: String) {
      userStatus.value = Status.LOADING

      val response = remoteSource.getForgetChangePassword(userId, password)

      if (response.isSuccessful) {
         userChangePassword.value = response.body()
         userStatus.value = Status.DONE
      } else if (!response.isSuccessful) {
         userChangePassword.value = false
         userStatus.value = Status.ERROR
      }

   }


   suspend fun signIn(userName: String) {
      userStatus.value = Status.LOADING
      val signIn = remoteSource.signIn(userName)

      if (signIn.isSuccessful) {
         userModel.value = signIn.body()
         userStatus.value = Status.DONE
      } else if (!signIn.isSuccessful) {

         userStatus.value = Status.ERROR

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
      userStatus.value = Status.LOADING
      val reference = db.reference
      val imagesReference = reference.child("profilpictures").child(imageName)
      imagesReference.putFile(selectedPicture!!).addOnSuccessListener {

         val uploadedPictureReference = db.reference.child("profilpictures").child(imageName)
         uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
            val profilePictureUrl = uri.toString()


            runBlocking {
               val signUp = remoteSource.createUser(
                  userName,
                  password,
                  name,
                  email,
                  phone,
                  profilePictureUrl,
                  date = date
               )

               if (signUp.isSuccessful) {

                  userStatus.value = Status.DONE
                  userSaved.value = signUp.body()
                  if (userSaved.value == false) {
                     deleteImage(profilePictureUrl)
                  }

               } else if (!signUp.isSuccessful) {
                  userStatus.value = Status.ERROR
                  deleteImage(profilePictureUrl)
                  Log.e("TAG", signUp.message())
                  Log.e("TAG", signUp.errorBody().toString())

               }
            }

         }
      }
   }

   private fun deleteImage(filename: String) {//apiden false döndüğünde firebase yüklenen fotoğrafı siliyorum

      val photoRef: StorageReference = db.getReferenceFromUrl(filename)
      photoRef.delete().addOnSuccessListener {
         // File deleted successfully
         Log.e("TAG", "Photo is delete")
      }.addOnFailureListener { // Uh-oh, an error occurred!
         userStatus.value = Status.ERROR
      }

   }

   suspend fun getLoginUserNameAndPassword(userName: String, password: String) {
      val response = remoteSource.getLoginUserNameAndPassword(userName, password)
      Log.e("TAG", "$userName,$password ")
      if (response.isSuccessful) {

         loginAuto.value = response.body()
      }
   }

}

