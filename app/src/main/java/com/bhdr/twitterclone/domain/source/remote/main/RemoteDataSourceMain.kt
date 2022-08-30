package com.bhdr.twitterclone.domain.source.remote.main

import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.data.model.remote.Users
import retrofit2.Response

interface RemoteDataSourceMain {
   suspend fun addTweet(
      userId: Int,
      content: String,
      image_url: String,
      date: String
   ): Response<Boolean>

   suspend fun postUserFollow(userId: Int, followId: Int): Response<Boolean>
   suspend fun getFollowedUserIdList(userId: Int): Response<List<Int>>
   suspend fun getSearchNotFollow(userId: Int): Response<List<Users>>
   suspend fun getTweets(userId: Int): Response<List<Posts>>
   suspend fun postLiked(likeUserId: Int, tweetId: Int, count: Int): Response<Int>
   suspend fun getPopularTags(): Response<List<String>>
   suspend fun getFollowCount(userId: Int): Response<Int>
   suspend fun getFollowedCount(userId: Int): Response<Int>
   suspend fun getTweetNew(tweetId: Int): Response<Posts>
}