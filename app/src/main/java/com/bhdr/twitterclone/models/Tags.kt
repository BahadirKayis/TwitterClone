package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Tags(
    @Json(name = "date")
    val date: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "post")
    val post: String?,
    @Json(name = "postId")
    val postId: Int?,
    @Json(name = "tagName")
    val tagName: String?
):Parcelable