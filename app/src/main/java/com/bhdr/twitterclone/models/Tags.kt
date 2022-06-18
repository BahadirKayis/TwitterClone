package com.bhdr.twitterclone.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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
)