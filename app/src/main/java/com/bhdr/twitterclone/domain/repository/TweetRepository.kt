package com.bhdr.twitterclone.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts

interface TweetRepository {
   suspend fun getTweets(id: Int)
   suspend fun tweetLiked(id: Int, count: Int, userId: Int)
   suspend fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?)
    suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>)
   suspend fun tweetsUpdate(tweets: List<Posts>)
   suspend fun getTweetsRoom()
   suspend fun tweetsRoomConvertAndAdd(lTweet: List<Posts?>)
   suspend fun getBitmap(url: String): Bitmap
   fun createImageUri(photoName: String): String
   fun storeBitmap(bmp: Bitmap)
   suspend fun isNewTweet(cloudTweet: List<Posts>, roomTweet: List<TweetsRoomModel>?)
   suspend fun newTweetButton(addTweet: List<Posts>?, updateTweet: List<Posts>?)
   suspend fun getFollowedUserIdList(userId: Int)
   fun tweetSignalR()
    fun signalRControl(
      id: Int,
      imageUrl: String
   )
   suspend fun notificationList()
}