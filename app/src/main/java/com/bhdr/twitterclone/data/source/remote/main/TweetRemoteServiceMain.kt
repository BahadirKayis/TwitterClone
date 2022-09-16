package com.bhdr.twitterclone.data.source.remote.main

import com.bhdr.twitterclone.common.Constants.CREATE_TWEET
import com.bhdr.twitterclone.common.Constants.FOLLOWED_COUNT
import com.bhdr.twitterclone.common.Constants.FOLLOW_COUNT
import com.bhdr.twitterclone.common.Constants.FOLLOW_USER_ID
import com.bhdr.twitterclone.common.Constants.NOT_FOLLOW
import com.bhdr.twitterclone.common.Constants.SEARCH_USER
import com.bhdr.twitterclone.common.Constants.TAGS
import com.bhdr.twitterclone.common.Constants.TWEETS
import com.bhdr.twitterclone.common.Constants.TWEETS_ONE
import com.bhdr.twitterclone.common.Constants.TWEET_LIKED
import com.bhdr.twitterclone.common.Constants.USER_FOLLOW
import com.bhdr.twitterclone.common.Constants.USER_INFO_ID
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.data.model.remote.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TweetRemoteServiceMain {

   @POST(CREATE_TWEET)
   suspend fun addTweet(
      @Query("userId") userId: Int,
      @Query("content") content: String,
      @Query("image_url") image_url: String,
      @Query("date") date: String
   ): Response<Boolean>

   @GET(USER_INFO_ID)
   suspend fun getUser(@Query("UserId") userId: Int): Response<Users>

   @POST(USER_FOLLOW)
   suspend fun postUserFollow(
      @Query("userId") userId: Int,
      @Query("followId") followId: Int
   ): Response<Boolean>

   @GET(FOLLOW_USER_ID)
   suspend fun getFollowedUserIdList(@Query("user_id") userId: Int): Response<List<Int>>

   @GET(NOT_FOLLOW)
   suspend fun getSearchNotFollow(@Query("id") userId: Int): Response<List<Users>>

   @GET(TWEETS)
   suspend fun getTweets(@Query("user_id") userId: Int): Response<List<Posts>>

   @POST(TWEET_LIKED)
   suspend fun postLiked(
      @Query("likeUserId") likeUserId: Int,
      @Query("Id") tweetId: Int,
      @Query("count") count: Int
   ): Response<Int>

   @GET(TAGS)
   suspend fun getPopularTags(): Response<List<String>>

   @GET(FOLLOW_COUNT)
   suspend fun getFollowCount(@Query("user_id") userId: Int): Response<Int>

   @GET(FOLLOWED_COUNT)
   suspend fun getFollowedCount(@Query("user_id") userId: Int): Response<Int>

   @GET(TWEETS_ONE)
   suspend fun getTweetNew(@Query("postId") tweetId: Int): Response<Posts>

   @GET(SEARCH_USER)
   suspend fun getSearchUser(@Query("userName") userName: String): Response<List<Users>>?
}