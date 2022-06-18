package com.bhdr.twitterclone.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Messages(
    @Json(name = "date")
    val date: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "messageContent")
    val messageContent: String?,
    @Json(name = "receiverUserId")
    val receiverUserId: Int?,
    @Json(name = "sendUser")
    val sendUser: String?,
    @Json(name = "sendUserId")
    val sendUserId: Int?
)