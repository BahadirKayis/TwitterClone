package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Followers(
   @SerializedName( "date")
   val date: String?,
   @SerializedName( "followed")
   val followed: Int?,
   @SerializedName( "followedNavigation")
   val followedNavigation: FollowedNavigation?,
   @SerializedName( "id")
   val id: Int?,
   @SerializedName( "user")
   val user: List<Users?>?,
   @SerializedName( "userId")
   val userId: Int?
) : Parcelable