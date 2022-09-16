package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Posts(
   @SerializedName( "date")
   val date: String?,
   @SerializedName("followers")
   val followers: List<Followers?>?,
   @SerializedName( "id")
   val id: Int?,
   @SerializedName( "postContent")
   val postContent: String?,
   @SerializedName( "postImageUrl")
   val postImageUrl: String?,
   @SerializedName( "postLike")
   var postLike: Int?,
   @SerializedName( "tags")
   val tags: List<Tags?>?,
   @SerializedName( "user")
   var user: Users?,
   @SerializedName("userId")
   val userId: Int?
) : Parcelable