package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize


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