package com.bhdr.twitterclone.data.model.locale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweets")
data class TweetsRoomModel(
   @ColumnInfo(name = "date")
   val date: String?,
   @ColumnInfo(name = "id")
   @PrimaryKey(autoGenerate = false)
   val id: Int?,
   @ColumnInfo(name = "postContent")
   val postContent: String?,
   @ColumnInfo(name = "tweetImage")
   var tweetImage: String?,
   @ColumnInfo(name = "postLike")
   var postLike: Int?,
   @ColumnInfo(name = "users")
   val user: UsersRoomModel?,
   @ColumnInfo(name = "userId")
   val userId: Int?,
   @ColumnInfo(name = "is_liked")
   var isLiked: Boolean? = false

)
