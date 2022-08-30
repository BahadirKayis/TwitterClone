package com.bhdr.twitterclone.data.source.remote.main

import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import retrofit2.Response

class RemoteDataSourceImplMain (private val tweetMain: TweetRemoteServiceMAIN):
   RemoteDataSourceMain {
   override suspend fun addTweet(
      userId: Int,
      content: String,
      image_url: String,
      date: String
   ): Response<Boolean> = tweetMain.addTweet(userId, content, image_url, date)

   override suspend fun postUserFollow(userId: Int, followId: Int): Response<Boolean> =
      tweetMain.postUserFollow(userId, followId)

   override suspend fun getFollowedUserIdList(userId: Int): Response<List<Int>> =
      tweetMain.getFollowedUserIdList(userId)

   override suspend fun getSearchNotFollow(userId: Int): Response<List<Users>> =
      tweetMain.getSearchNotFollow(userId)

   override suspend fun getTweets(userId: Int): Response<List<Posts>> =
      tweetMain.getTweets(userId)

   override suspend fun postLiked(likeUserId: Int, tweetId: Int, count: Int): Response<Int> =
      tweetMain.postLiked(likeUserId, tweetId, count)

   override suspend fun getPopularTags(): Response<List<String>> = tweetMain.getPopularTags()

   override suspend fun getFollowCount(userId: Int): Response<Int> =
      tweetMain.getFollowCount(userId)

   override suspend fun getFollowedCount(userId: Int): Response<Int> =
      tweetMain.getFollowedCount(userId)

   override suspend fun getTweetNew(tweetId: Int): Response<Posts> =
      tweetMain.getTweetNew(tweetId)
}