package com.bhdr.twitterclone.room

import androidx.room.TypeConverter
import com.bhdr.twitterclone.models.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class Converters {
   @TypeConverter
   fun fromUser(user: UsersRoomModel): String {
//      return JSONObject().apply {
//         put("id", user.id)
//         put("name", user.name)
//         put("photoUrl", user.photoUrl)
//         put("userName", user.userName)
//      }.toString()
    return Gson().toJson(user)
   }

   @TypeConverter
   fun toUser(user: String): UsersRoomModel {
      val objectType = object : TypeToken<UsersRoomModel>() {}.type
      return Gson().fromJson(user, objectType)
//      val json = JSONObject(user)
//      return Users(
//         json.get("id"),
//         json.getString("name"),
//         json.getString("photoUrl"),
//         json.getString("userName")
//      )
   }
}
