package com.bhdr.twitterclone.data.repos

import android.net.Uri
import android.util.Log
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.runBlocking


class LoginUpRepositoryImpl(
   private val remoteSource: RemoteDataSourceLogin,
   private val FirebaseStorage: FirebaseStorage
) :
   LoginUpRepository {


   override suspend fun userForgetId(userName: String): Int? =
      remoteSource.getForgetPassword(userName).body()

   override suspend fun userChangePassword(userId: Int, password: String): Boolean? =
      remoteSource.getForgetChangePassword(userId, password).body()


   override suspend fun signIn(userName: String): Users? = remoteSource.signIn(userName).body()


   override suspend fun signUP(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      date: String?,
      imageName: String,
      selectedPicture: Uri?
   ): Boolean {
      var isSaved = false
      val reference = FirebaseStorage.reference
      val imagesReference = reference.child("profilpictures").child(imageName)
      imagesReference.putFile(selectedPicture!!).addOnSuccessListener {

         val uploadedPictureReference =
            FirebaseStorage.reference.child("profilpictures").child(imageName)
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


                  isSaved = signUp.body()!!
                  if (!isSaved) {
                     deleteImage(profilePictureUrl)

                  }

               } else if (!signUp.isSuccessful) {

                  deleteImage(profilePictureUrl)
                  isSaved = false
                  Log.e("signUpError", signUp.errorBody().toString())

               }
            }

         }
      }
      return isSaved
   }

   override fun deleteImage(filename: String) {//apiden false döndüğünde firebase yüklenen fotoğrafı siliyorum

      val photoRef: StorageReference = FirebaseStorage.getReferenceFromUrl(filename)
      photoRef.delete().addOnSuccessListener {
         // File deleted successfully
         Log.e("ImageAddUser", "Photo is delete")
      }.addOnFailureListener { // Uh-oh, an error occurred!
         Log.e("deleteImageAddUser", "Failed to delete photo")
      }

   }

   override suspend fun getLoginUserNameAndPassword(userName: String, password: String): Boolean? =
      remoteSource.getLoginUserNameAndPassword(userName, password).body()
}

