package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
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
   val user: List<Users?>?,
   @Json(name = "userId")
   val userId: Int?
) : Parcelable