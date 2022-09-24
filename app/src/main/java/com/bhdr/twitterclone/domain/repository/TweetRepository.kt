package com.bhdr.twitterclone.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts

interface TweetRepository {
   suspend fun getTweets(id: Int): List<Posts>?
   suspend fun tweetLiked(id: Int, count: Int, userId: Int)
   suspend fun addTweet(
      id: Int,
      tweetText: String,
      tweetImageName: String,
      tweetImage: Uri?, result: (Boolean) -> Unit
   )

   suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>): List<TweetsRoomModel>?
   suspend fun tweetsUpdate(tweets: List<Posts>): List<TweetsRoomModel>?
   suspend fun getTweetsRoom(): List<TweetsRoomModel>?
   suspend fun tweetsRoomConvertAndAdd(lTweet: List<Posts?>): List<TweetsRoomModel>?
   suspend fun getBitmap(url: String): Bitmap
   fun createImageUri(photoName: String): String
   fun storeBitmap(bmp: Bitmap)
   suspend fun isNewTweet(
      cloudTweet: List<Posts>,
      roomTweet: List<TweetsRoomModel>?
   ): Pair<List<Posts>, List<Posts>?>

   suspend fun getFollowedUserIdList(userId: Int)
   suspend fun signalRControl(
      id: Int,
      imageUrl: String
   )
   suspend fun notificationList(): List<Any>

   fun hashMapNewTweet(): MutableLiveData<HashMap<Int, String>>

}