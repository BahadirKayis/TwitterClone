package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class FollowedNavigation(
    @Json(name = "date")
    val date: String?,
    @Json(name = "followers")
    val followers: List<String?>?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "postContent")
    val postContent: String?,
    @Json(name = "postImageUrl")
    val postImageUrl: String?,
    @Json(name = "postLike")
    val postLike: Int?,
    @Json(name = "tags")
    val tags: List<Tags?>?,
    @Json(name = "user")
    val user: String?,
    @Json(name = "userId")
    val userId: Int?
):Parcelable