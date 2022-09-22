package com.bhdr.twitterclone.domain.source.locale

import com.bhdr.twitterclone.data.model.locale.NotificationsDataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel

interface LocalDataSource {
   suspend fun addTweet(tweet: List<TweetsRoomModel?>)
   suspend fun updateTweet(tweetId: Int, postLikeCount: Int)
   suspend fun tweetIsLiked(tweetId: Int, postLikeCount: Int, liked: Boolean)
   suspend fun deleteTweet(): Int?
   suspend fun allTweet(): List<TweetsRoomModel>?
   suspend fun addNotificationTweet(tweet: NotificationsDataItem.NotificationTweet)
   suspend fun addNotificationLike(tweet: NotificationsDataItem.NotificationLike)
   suspend fun notificationListTweet(): List<NotificationsDataItem.NotificationTweet>
   suspend fun notificationListLike(): List<NotificationsDataItem.NotificationLike>
   suspend fun notificationDeleteLike()
   suspend fun notificationDeleteTweet()
   suspend fun isTweetQuery(tweetId: Int): List<NotificationsDataItem.NotificationLike>?
}