package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Posts(
   @Json(name = "date")
   val date: String?,
   @Json(name = "followers")
   val followers: List<Followers?>?,
   @Json(name = "id")
   val id: Int?,
   @Json(name = "postContent")
   val postContent: String?,
   @Json(name = "postImageUrl")
   val postImageUrl: String?,
   @Json(name = "postLike")
   var postLike: Int?,
   @Json(name = "tags")
   val tags: List<Tags?>?,
   @Json(name = "user")
   var user: Users?,
   @Json(name = "userId")
   val userId: Int?
) : Parcelable