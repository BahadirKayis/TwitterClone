package com.bhdr.twitterclone.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhdr.twitterclone.common.Converters
import com.bhdr.twitterclone.data.model.locale.NotificationsDataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel


@Database(
   entities = [TweetsRoomModel::class, NotificationsDataItem.NotificationTweet::class, NotificationsDataItem.NotificationLike::class],
   version = 1,
   exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TweetsDatabase : RoomDatabase() {


   abstract fun tweetDao(): TweetDaoInterface

}
