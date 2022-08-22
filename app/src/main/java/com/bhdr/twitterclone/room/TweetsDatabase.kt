package com.bhdr.twitterclone.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhdr.twitterclone.helperclasses.DataItem


@Database(
   entities = [TweetsRoomModel::class, DataItem.NotificationTweet::class, DataItem.NotificationLike::class],
   version = 4,
   exportSchema = false
)
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
