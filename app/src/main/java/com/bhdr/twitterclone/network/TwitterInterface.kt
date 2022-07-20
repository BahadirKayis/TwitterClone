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


    @POST("postSignUp")
    suspend fun createUser(
        @Query("user_name") userName: String,
        @Query("user_password") password: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("photo_url") photoUrl: String,
        @Query("date") date: String?
    ): Response<Boolean>

    @GET("getUserNameAndEmail")
    suspend fun getUsernameAndEmail(): Response<List<UsernameAndEmailControl>>

    @GET("getLoginUserName")
    suspend fun signIn(@Query("userControl") userName: String): Response<Users>

    @GET("getForgetPassword")
    suspend fun getForgetPassword(@Query("userControl") userName: String): Response<Int>

    @POST("updatePassword")
    suspend fun getForgetChangePassword(
        @Query("user_id") userId: Int,
        @Query("password") password: String
    ): Response<Boolean>

    @GET("getPosts")
    suspend fun getPosts(@Query("id") id: Int): Response<List<Posts>>

    @POST("postLiked")
    suspend fun postLiked(@Query("Id") id: Int, @Query("count") count: Int): Response<Int>

    @GET("getTags")
    suspend fun getPopularTags(): Response<List<String>>

    @GET("getSearchNotFollow")
    suspend fun getSearchNotFollow(@Query("id") id: Int): Response<List<Users>>

    @POST("postUserFollow")
    suspend fun postUserFollow(
        @Query("userId") userId: Int,
        @Query("followId") followId: Int
    ): Response<Boolean>

    @POST("postCreatePost")
    suspend fun addTweet(
        @Query("userId") userId: Int,
        @Query("content") content: String,
        @Query("image_url") image_url: String
    ): Response<Boolean>
}