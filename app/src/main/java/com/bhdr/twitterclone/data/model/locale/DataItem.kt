package com.bhdr.twitterclone.data.model.locale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class DataItem {
   @Entity(tableName = "notificationLike")
   data class NotificationLike(

      @PrimaryKey(autoGenerate = true)
      val roomId: Int? = 0,
      @ColumnInfo(name = "id")
      var id: Int?,
      @ColumnInfo(name = "imageUrl")
      var imageUrl: String?,
      @ColumnInfo(name = "userName")
      var userName: String?,
      @ColumnInfo(name = "name")
      var name: String?,
      @ColumnInfo(name = "date")
      var date: String?,
      @ColumnInfo(name = "tweets")
      val tweet: TweetsRoomModel?
   ) : DataItem()

   @Entity(tableName = "notificationTweet")
   data class NotificationTweet(
      @PrimaryKey(autoGenerate = true)
      val roomId: Int? = 0,
      var id: Int?,
      var imageUrl: String?,
      var userName: String?,
      var name: String?,
      var date: String?,
      val tweet: TweetsRoomModel?
   ) : DataItem()

}