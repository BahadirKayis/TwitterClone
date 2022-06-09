package com.bhdr.twitterclone.Models

import com.squareup.moshi.Json
import java.util.*

data class Tags
    (@Json(name="Id") var id:Int,
    @Json(name="PostId") var postId:Int,
    @Json(name="TagName") var tagName:String,
    @Json(name="Date") var date:Date,
    @Json(name="Posts") var post:Posts
     )
