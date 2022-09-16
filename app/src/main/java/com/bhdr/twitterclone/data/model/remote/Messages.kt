package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Messages(
   @SerializedName( "date")
   val date: String?,
   @SerializedName( "id")
   val id: Int?,
   @SerializedName( "messageContent")
   val messageContent: String?,
   @SerializedName( "receiverUserId")
   val receiverUserId: Int?,
   @SerializedName( "sendUser")
   val sendUser: String?,
   @SerializedName( "sendUserId")
   val sendUserId: Int?
) : Parcelable