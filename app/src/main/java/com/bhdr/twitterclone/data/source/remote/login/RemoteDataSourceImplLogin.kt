package com.bhdr.twitterclone.data.source.remote.login

import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import retrofit2.Response

class RemoteDataSourceImplLogin constructor(private val tweetInterFace: TweetRemoteServiceLogin) :
   RemoteDataSourceLogin {
   override suspend fun createUser(
      userName: String,
      password: String,
      name: String,
      email: String,
      phone: String,
      photoUrl: String,
      date: String?
   ): Response<Boolean> =
      tweetInterFace.createUser(userName, password, name, email, phone, photoUrl, date)

   override suspend fun signIn(userName: String): Response<Users> = tweetInterFace.signIn(userName)

   override suspend fun getLoginUserNameAndPassword(
      userName: String,
      password: String
   ): Response<Boolean> = tweetInterFace.getLoginUserNameAndPassword(userName, password)

   override suspend fun getForgetChangePassword(userId: Int, password: String): Response<Boolean> =
      tweetInterFace.getForgetChangePassword(userId, password)

   override suspend fun getForgetPassword(userName: String): Response<Int> =
      getForgetPassword(userName)


}