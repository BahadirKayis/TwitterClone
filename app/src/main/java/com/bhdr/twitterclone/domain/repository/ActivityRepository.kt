package com.bhdr.twitterclone.domain.repository

import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts

interface ActivityRepository {
   suspend fun followCount(userId: Int): Int?
   suspend fun followedCount(userId: Int): Int?
   suspend fun getFollowedUserIdList(userId: Int)

   //fun tweetSignalR(userId: Int)
   suspend fun deleteAllRoom(): Boolean
   fun tweetsRoomConvertAndAdd(tweet: Posts): TweetsRoomModel
   suspend fun tweetNew(tweetId: Int): Posts
   suspend fun signalRControl(
      id: Int,
      imageUrl: String,
      userName: String,
      name: String,
      postId: Int,
      post: Posts?,result: (Boolean) -> Unit
   )


}