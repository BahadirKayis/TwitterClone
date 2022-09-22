package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
   @SerializedName("date")
   val date: String?,
   @SerializedName("email")
   val email: String?,
   @SerializedName("followers")
   val followers: List<Followers>?,
   @SerializedName("id")
   var id: Int?,
   @SerializedName("messages")
   val messages: List<Messages>?,
   @SerializedName("name")
   var name: String?,
   @SerializedName("phone")
   val phone: String?,
   @SerializedName("photoUrl")
   var photoUrl: String?,
   @SerializedName("posts")
   val posts: List<Posts>?,
   @SerializedName("userName")
   var userName: String?,
   @SerializedName("userPassword")
   val userPassword: String?
) : Parcelable
