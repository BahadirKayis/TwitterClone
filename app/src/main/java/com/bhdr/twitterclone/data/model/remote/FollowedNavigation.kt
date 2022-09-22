package com.bhdr.twitterclone.data.model.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowedNavigation(
   @SerializedName("date")
   val date: String?,
   @SerializedName("followers")
   val followers: List<String?>?,
   @SerializedName("id")
   val id: Int?,
   @SerializedName("postContent")
   val postContent: String?,
   @SerializedName("postImageUrl")
   val postImageUrl: String?,
   @SerializedName("postLike")
   val postLike: Int?,
   @SerializedName("tags")
   val tags: List<Tags?>?,
   @SerializedName("user")
   val user: String?,
   @SerializedName("userId")
   val userId: Int?
) : Parcelable