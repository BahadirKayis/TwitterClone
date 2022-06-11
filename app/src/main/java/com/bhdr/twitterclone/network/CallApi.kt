package com.bhdr.twitterclone.network

object CallApi {
    val  retrofitService:TwitterInterface by lazy { retrofit.create(TwitterInterface::class.java)}

}