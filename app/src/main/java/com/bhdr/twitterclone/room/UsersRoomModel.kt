package com.bhdr.twitterclone.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsersRoomModel(
   @ColumnInfo(name = "id")
   @PrimaryKey(autoGenerate = false)
   var id: Any?,
   @ColumnInfo(name = "photoUrl")
   var photoUrl: String?,
   @ColumnInfo(name = "userName")
   var userName: String?,
   @ColumnInfo(name = "name")
   var name: String?,
)
