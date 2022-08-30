package com.bhdr.twitterclone.domain.source.locale

import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.DataItem

interface LocalDataSource {
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)
   suspend fun updateTweet(tweetId: Int, postLikeCount: Int)
   suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean)
   suspend fun deleteTweet(): Int?
   suspend fun allTweet(): List<TweetsRoomModel>?
   suspend fun addNotificationTweet(tweet: DataItem.NotificationTweet)
   suspend fun addNotificationLike(tweet: DataItem.NotificationLike)
   suspend fun notificationListTweet(): List<DataItem.NotificationTweet>
   suspend fun notificationListLike(): List<DataItem.NotificationLike>
   suspend fun notificationDeleteLike()
   suspend fun notificationDeleteTweet()
}