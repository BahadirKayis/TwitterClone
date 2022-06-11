package com.bhdr.twitterclone.models

import com.squareup.moshi.Json
import java.util.*

data class Users(
    @Json(name="Id") var id:Int,
    @Json(name="UserName") var userName:String,
    @Json(name="UserPassword") var password:String,
    @Json(name="Name") var name:String,
    @Json(name="Email") var eMail:String,
    @Json(name="Phone") var phone:String,
    @Json(name="PhotoUrl") var imgUrl:String,
    @Json(name="Date") var date:Date,
    @Json(name="Followers") var followers:List<Followers>,
    @Json(name="Messages") var messages:List<Messages>,
    @Json(name="Posts") var posts:List<Posts>,

    )
