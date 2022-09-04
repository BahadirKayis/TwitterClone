package com.bhdr.twitterclone.common

import androidx.room.TypeConverter
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
   @TypeConverter
   fun fromUser(user: UsersRoomModel): String {
      return Gson().toJson(user)
   }

   @TypeConverter
   fun toUser(user: String): UsersRoomModel {
      val objectType = object : TypeToken<UsersRoomModel>() {}.type
      return Gson().fromJson(user, objectType)
   }

   @TypeConverter
   fun fromTweetDataItem(tweetRoom: TweetsRoomModel): String {
      return Gson().toJson(tweetRoom)
   }

   @TypeConverter
   fun toTweetDataItem(tweetRoom: String): TweetsRoomModel {
      val objectType = object : TypeToken<TweetsRoomModel>() {}.type
      return Gson().fromJson(tweetRoom, objectType)
   }
}
