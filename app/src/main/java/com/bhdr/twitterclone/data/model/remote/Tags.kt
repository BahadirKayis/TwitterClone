package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
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