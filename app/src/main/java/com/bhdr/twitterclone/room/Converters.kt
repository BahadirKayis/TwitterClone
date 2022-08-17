package com.bhdr.twitterclone.room

import androidx.room.TypeConverter
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
//
//   @TypeConverter
//   fun fromTweetDataDate(date: Calendar): String {
//      return Gson().toJson(date)
//   }
//
//   @TypeConverter
//   fun toTweetDataDate(date: String): Calendar {
//      val objectType = object : TypeToken<Calendar>() {}.type
//      return Gson().fromJson(date, objectType)
//   }
}
