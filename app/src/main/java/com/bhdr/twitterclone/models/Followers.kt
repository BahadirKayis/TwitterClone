package com.bhdr.twitterclone.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Followers(
    @Json(name = "date")
    val date: String?,
    @Json(name = "followed")
    val followed: Int?,
    @Json(name = "followedNavigation")
    val followedNavigation: FollowedNavigation?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "user")
    val user: String?,
    @Json(name = "userId")
    val userId: Int?
)