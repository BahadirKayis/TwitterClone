package com.bhdr.twitterclone.data.source.remote.login


import com.bhdr.twitterclone.common.Constants.FORGET_PASSWORD
import com.bhdr.twitterclone.common.Constants.LOGIN_NAME
import com.bhdr.twitterclone.common.Constants.LOGIN_NAME_PASSWORD
import com.bhdr.twitterclone.common.Constants.SIGN_UP
import com.bhdr.twitterclone.common.Constants.UPDATE_PASSWORD
import com.bhdr.twitterclone.common.Constants.USER_INFO_ID
import com.bhdr.twitterclone.data.model.remote.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TweetRemoteServiceLOGIN {
   @POST(SIGN_UP)
   suspend fun createUser(
      @Query("user_name") userName: String,
      @Query("user_password") password: String,
      @Query("name") name: String,
      @Query("email") email: String,
      @Query("phone") phone: String,
      @Query("photo_url") photoUrl: String,
      @Query("date") date: String?
   ): Response<Boolean>

   @GET(USER_INFO_ID)
   suspend fun getUser(@Query("UserId") userId: Int): Response<Users>

//   @GET("userNameAndEmail")
//   suspend fun getUsernameAndEmail(): Response<List<UsernameAndEmailControl>>

   @GET(LOGIN_NAME)
   suspend fun signIn(@Query("userControl") userName: String): Response<Users>

   @GET(LOGIN_NAME_PASSWORD)
   suspend fun getLoginUserNameAndPassword(
      @Query("userName") userName: String,
      @Query("password") password: String
   ): Response<Boolean>

   @POST(UPDATE_PASSWORD)
   suspend fun getForgetChangePassword(
      @Query("user_id") userId: Int,
      @Query("password") password: String
   ): Response<Boolean>

   @GET(FORGET_PASSWORD)
   suspend fun getForgetPassword(@Query("userControl") userName: String): Response<Int>

   //MainController

}