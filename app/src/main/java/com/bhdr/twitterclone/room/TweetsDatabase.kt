package com.bhdr.twitterclone.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [TweetsRoomModel::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class TweetsDatabase : RoomDatabase() {

   abstract fun tweetDao(): TweetDaoInterface

   companion object {
      private var INSTANCE: TweetsDatabase? = null

      fun getTweetsDatabase(context: Context): TweetsDatabase? {

         synchronized(TweetsDatabase::class) {
            var instance = INSTANCE
            if (instance == null) {
               instance = Room.databaseBuilder(
                  context,
                  TweetsDatabase::class.java,
                  "tweetsdatabese.db"
               )
                  .fallbackToDestructiveMigration()
                  .build()

            }
            INSTANCE = instance
         }
         return INSTANCE
      }
   }
}