package com.bhdr.twitterclone.domain.repository

import android.net.Uri
import com.bhdr.twitterclone.data.model.remote.Users

interface LoginUpRepository {
   suspend fun userForgetId(userName: String): Int?
   suspend fun userChangePassword(userId: Int, password: String): Boolean?
   suspend fun signIn(userName: String): Users?
   suspend fun signUP(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      imageName: String,
      selectedPicture: Uri?
   ): Boolean

   suspend fun getLoginUserNameAndPassword(userName: String, password: String): Boolean?
   fun deleteImage(filename: String)
}