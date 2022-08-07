package com.bhdr.twitterclone.room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

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
   fun fromBitmap(bitmap: Bitmap): ByteArray {
      val outputStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      return outputStream.toByteArray()
   }

   @TypeConverter
   fun toBitmap(byteArray: ByteArray): Bitmap {
      return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
   }
}
