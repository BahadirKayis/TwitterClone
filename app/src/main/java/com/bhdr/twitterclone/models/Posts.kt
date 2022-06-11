package com.bhdr.twitterclone.models

import android.nfc.Tag
import com.squareup.moshi.Json
import java.util.*

data class Posts
    (@Json(name="Id") var id:Int,
     @Json(name="UserId") var userId:Int,
     @Json(name="PostContent") var content:String,
     @Json(name="PostImageUrl") var imageUrl:String,
     @Json(name="PostLike") var like:Int?,
     @Json(name="Date") var date:Date?,
     @Json(name="User") var user:Users?,
     @Json(name="Followers") var followers:Followers,
     @Json(name="Tags") var tags:Tag?

        )