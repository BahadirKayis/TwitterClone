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

}
