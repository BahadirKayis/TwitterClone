package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize


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