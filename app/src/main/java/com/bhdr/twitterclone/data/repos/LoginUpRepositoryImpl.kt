package com.bhdr.twitterclone.data.repos

import android.net.Uri
import android.util.Log
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Named


class LoginUpRepositoryImpl(
   private val remoteSource: RemoteDataSourceLogin,
   private val FirebaseStorage: FirebaseStorage,
    private val coContextIO: CoroutineDispatcher
) :
   LoginUpRepository {


   override suspend fun userForgetId(userName: String): Int? =
      remoteSource.getForgetPassword(userName).body()

   override suspend fun userChangePassword(userId: Int, password: String): Boolean? =
      remoteSource.getForgetChangePassword(userId, password).body()


   override suspend fun signIn(userName: String): Users? = remoteSource.signIn(userName).body()

   private var job: Job? = null
   override suspend fun signUP(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      imageName: String,
      selectedPicture: Uri?, result: (Boolean) -> Unit
   ) {

      val imagesReference = FirebaseStorage.reference.child("profilpictures").child(imageName)
      imagesReference.putFile(selectedPicture!!).addOnSuccessListener {

         val uploadedPictureReference =
            FirebaseStorage.reference.child("profilpictures").child(imageName)
         uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
            val profilePictureUrl = uri.toString()

            job = CoroutineScope(coContextIO).launch {
               val signUp = remoteSource.createUser(
                  userName,
                  password,
                  name,
                  email,
                  phone,
                  profilePictureUrl,
               )
               if (signUp.isSuccessful) {
                  result(signUp.body()!!)
               } else if (!signUp.isSuccessful) {

                  deleteImage(profilePictureUrl)
                  result(false)

               }
            }

         }
      }
   }


   override fun deleteImage(filename: String) {//apiden false döndüğünde firebase yüklenen fotoğrafı siliyorum
      val photoRef: StorageReference = FirebaseStorage.getReferenceFromUrl(filename)
      photoRef.delete().addOnSuccessListener {
         // File deleted successfully
         Log.i("ImageAddUser", "Photo is delete")
      }.addOnFailureListener { // Uh-oh, an error occurred!
         Log.e("deleteImageAddUser", "Failed to delete photo")
      }

   }

   override suspend fun getLoginUserNameAndPassword(userName: String, password: String): Boolean? =
      remoteSource.getLoginUserNameAndPassword(userName, password).body()
}

