package com.bhdr.twitterclone.network

import com.bhdr.twitterclone.models.UsernameAndEmailControl
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface TwitterInterface {


    @POST("postSignUp")
      fun createUser(@Query("user_name") userName: String,@Query("user_password") password: String,@Query("name") name:String
                           ,@Query("email") email:String,@Query("phone") phone:String,@Query("photo_url") photoUrl:String,@Query("date") date: String?):Call<Boolean>
    @GET("getUserNameAndEmail")
     fun getUsernameAndEmail():Call<List<UsernameAndEmailControl>>
}