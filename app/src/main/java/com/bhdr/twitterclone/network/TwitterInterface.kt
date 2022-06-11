package com.bhdr.twitterclone.network

import com.bhdr.twitterclone.models.UsernameAndEmailControl
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*

interface TwitterInterface {
    @POST("postSignUp")
    suspend fun createUser(userName: String, password: String,name:String,email:String,phone:String,photoUrl:String,date: String?):Boolean
    @GET("getUserNameAndEmail")
    suspend fun getUsernameAndEmail():List<UsernameAndEmailControl>
}