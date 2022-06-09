package com.bhdr.twitterclone.Models

import com.squareup.moshi.Json
import java.util.*

data class Followers(
    @Json(name="Id") var id:Int,
    @Json(name="UserId") var userId:Int,
    @Json(name="Followed") var followedId:Int,
    @Json(name="Date") var Date:Date?,
    @Json(name="FollowedNavigation") var posts: Posts,
    @Json(name="User") var user:Users
)
