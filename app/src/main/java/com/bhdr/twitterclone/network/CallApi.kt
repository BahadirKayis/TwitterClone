package com.bhdr.twitterclone.network

object CallApi {
   val retrofitServiceLogInUp: TwitterInterface by lazy { retrofitLogInUp.create(TwitterInterface::class.java) }
   val retrofitServiceMain: TwitterInterface by lazy { retrofitMain.create(TwitterInterface::class.java) }


}