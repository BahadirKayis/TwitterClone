package com.bhdr.twitterclone.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhdr.twitterclone.data.model.locale.DataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.common.Converters


@Database(
   entities = [TweetsRoomModel::class, DataItem.NotificationTweet::class, DataItem.NotificationLike::class],
   version = 1,
   exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TweetsDatabase : RoomDatabase() {


   abstract fun tweetDao(): TweetDaoInterface

}
