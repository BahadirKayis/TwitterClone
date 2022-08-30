package com.bhdr.twitterclone.data.source.locale

import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.DataItem
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import javax.inject.Inject

class LocalDatabaseImpl @Inject constructor(private val localDatabase: TweetDaoInterface) :
   LocalDataSource {
   override suspend fun addTweet(tweet: List<TweetsRoomModel?>) = localDatabase.addTweet(tweet)

   override suspend fun updateTweet(tweetId: Int, postLikeCount: Int) =
      localDatabase.updateTweet(tweetId, postLikeCount)

   override suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean) =
      localDatabase.tweetIsLiked(tweetId, postLikeCount, liked)

   override suspend fun deleteTweet(): Int? = localDatabase.deleteTweet()

   override suspend fun allTweet(): List<TweetsRoomModel>? = localDatabase.allTweet()

   override suspend fun addNotificationTweet(tweet: DataItem.NotificationTweet) =
      localDatabase.addNotificationTweet(tweet)

   override suspend fun addNotificationLike(tweet: DataItem.NotificationLike) =
      localDatabase.addNotificationLike(tweet)

   override suspend fun notificationListTweet(): List<DataItem.NotificationTweet> =
      localDatabase.notificationListTweet()

   override suspend fun notificationListLike(): List<DataItem.NotificationLike> =
      localDatabase.notificationListLike()

   override suspend fun notificationDeleteLike() = localDatabase.notificationDeleteTweet()
   override suspend fun notificationDeleteTweet() = localDatabase.notificationDeleteTweet()
}