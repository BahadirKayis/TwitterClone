package com.bhdr.twitterclone.common

import com.microsoft.signalr.HubConnectionBuilder

object Constants {
   const val BASE_URL_LOGIN = "https://bahadir.codingwithlove.com/api/LogInUp/"

   const val SIGN_UP = "signUp"
   const val LOGIN_NAME = "loginUserName"
   const val LOGIN_NAME_PASSWORD = "loginUserNameAndPassword"
   const val UPDATE_PASSWORD = "updatePassword"
   const val FORGET_PASSWORD = "forgetPassword"


   const val BASE_URL_MAIN = "https://bahadir.codingwithlove.com/api/Main/"

   const val USER_INFO_ID = "user"
   const val CREATE_TWEET = "createTweet"
   const val USER_FOLLOW = "userFollow"
   const val FOLLOW_USER_ID = "followedUserIdList"
   const val NOT_FOLLOW = "searchNotFollow"
   const val TWEETS = "tweets"
   const val TWEET_LIKED = "tweetLiked"
   const val TAGS = "tags"
   const val FOLLOW_COUNT = "followCount"
   const val FOLLOWED_COUNT = "followedCount"
   const val TWEETS_ONE = "TweetNew"
   const val SEARCH_USER = "searchUserName"

   val hubConnection =
      HubConnectionBuilder.create("https://bahadir.codingwithlove.com/newTweetHub").build()!!
}