package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Users(
   @Json(name = "date")
   val date: String?,
   @Json(name = "email")
   val email: String?,
   @Json(name = "followers")
   val followers: List<Followers>?,
   @Json(name = "id")
   var id: Int?,
   @Json(name = "messages")
   val messages: List<Messages>?,
   @Json(name = "name")
   var name: String?,
   @Json(name = "phone")
   val phone: String?,
   @Json(name = "photoUrl")
   var photoUrl: String?,
   @Json(name = "posts")
   val posts: List<Posts>?,
   @Json(name = "userName")
   var userName: String?,
   @Json(name = "userPassword")
   val userPassword: String?
) : Parcelable
