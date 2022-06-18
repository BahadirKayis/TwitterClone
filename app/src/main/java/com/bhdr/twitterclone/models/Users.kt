package com.bhdr.twitterclone.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Users(
    @Json(name = "date")
    val date: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "followers")
    val followers: List<Followers>?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "messages")
    val messages: List<Messages>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "photoUrl")
    val photoUrl: String?,
    @Json(name = "posts")
    val posts: List<Posts>?,
    @Json(name = "userName")
    val userName: String?,
    @Json(name = "userPassword")
    val userPassword: String?
)