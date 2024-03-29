package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class Tags(
   @SerializedName("date")
   val date: String?,
   @SerializedName("id")
   val id: Int?,
   @SerializedName("post")
   val post: String?,
   @SerializedName("postId")
   val postId: Int?,
   @SerializedName("tagName")
   val tagName: String?
) : Parcelable