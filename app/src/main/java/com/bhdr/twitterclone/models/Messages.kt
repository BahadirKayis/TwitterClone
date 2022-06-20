package com.bhdr.twitterclone.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
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
): Parcelable