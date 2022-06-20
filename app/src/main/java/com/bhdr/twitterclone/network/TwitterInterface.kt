package com.bhdr.twitterclone.network

import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.UsernameAndEmailControl
import com.bhdr.twitterclone.models.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface TwitterInterface {


    @POST("postSignUp")
    fun createUser(
        @Query("user_name") userName: String,
        @Query("user_password") password: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("photo_url") photoUrl: String,
        @Query("date") date: String?
    ): Call<Boolean>

    @GET("getUserNameAndEmail")
    fun getUsernameAndEmail(): Call<List<UsernameAndEmailControl>>

    @GET("getLoginUserName")
    suspend fun signIn(@Query("userControl") userName: String): Response<Users>

    @GET("getForgetPassword")
    fun getForgetPassword(@Query("userControl") userName: String): Call<Int>

    @POST("updatePassword")
    fun getForgetChangePassword(
        @Query("user_id") userId: Int,
        @Query("password") password: String
    ): Call<Boolean>

    suspend fun getPosts(@Query("id")id:Int):Response<List<Posts>>

}