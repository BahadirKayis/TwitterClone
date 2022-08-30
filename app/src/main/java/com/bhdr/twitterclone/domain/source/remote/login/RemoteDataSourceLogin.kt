package com.bhdr.twitterclone.domain.source.remote.login

import com.bhdr.twitterclone.data.model.remote.Users
import retrofit2.Response

interface RemoteDataSourceLogin {
   suspend fun createUser(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      photoUrl: String,
      date: String?
   ): Response<Boolean>

   suspend fun getUser(userId: Int): Response<Users>
   suspend fun signIn(userName: String): Response<Users>
   suspend fun getLoginUserNameAndPassword(
      userName: String,
      password: String
   ): Response<Boolean>

   suspend fun getForgetChangePassword(userId: Int, password: String): Response<Boolean>

   suspend fun getForgetPassword(userName: String): Response<Int>

}