package com.bhdr.twitterclone.network

import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Tags
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.models.Users

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface TwitterInterface {

   //LoginUpController
   @POST("signUp")
   suspend fun createUser(
      @Query("user_name") userName: String,
      @Query("user_password") password: String,
      @Query("name") name: String,
      @Query("email") email: String,
      @Query("phone") phone: String,
      @Query("photo_url") photoUrl: String,
      @Query("date") date: String?
   ): Response<Boolean>

   @GET("userNameAndEmail")
   suspend fun getUsernameAndEmail(): Response<List<UsernameAndEmailControl>>

   @GET("loginUserName")
   suspend fun signIn(@Query("userControl") userName: String): Response<Users>

   @GET("loginUserNameAndPassword")
   suspend fun getLoginUserNameAndPassword(
      @Query("userName") userName: String,
      @Query("password") password: String
   ): Response<Boolean>

   @POST("updatePassword")
   suspend fun getForgetChangePassword(
      @Query("user_id") userId: Int,
      @Query("password") password: String
   ): Response<Boolean>

   @GET("forgetPassword")
   suspend fun getForgetPassword(@Query("userControl") userName: String): Response<Int>

   //MainController
   @POST("createTweet")
   suspend fun addTweet(
      @Query("userId") userId: Int,
      @Query("content") content: String,
      @Query("image_url") image_url: String
   ): Response<Boolean>

   @POST("userFollow")
   suspend fun postUserFollow(
      @Query("userId") userId: Int,
      @Query("followId") followId: Int
   ): Response<Boolean>

   @GET("followedUserIdList")
   suspend fun getFollowedUserIdList(@Query("user_id") userId: Int): Response<List<Int>>

   @GET("searchNotFollow")
   suspend fun getSearchNotFollow(@Query("id") userId: Int): Response<List<Users>>

   @GET("tweets")
   suspend fun getTweets(@Query("user_id") userId: Int): Response<List<Posts>>

   @POST("tweetLiked")
   suspend fun postLiked(@Query("Id") tweetId: Int, @Query("count") count: Int): Response<Int>

   @GET("tags")
   suspend fun getPopularTags(): Response<List<String>>


}