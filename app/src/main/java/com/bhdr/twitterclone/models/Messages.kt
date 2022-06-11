package com.bhdr.twitterclone.models

import com.squareup.moshi.Json
import java.util.*

data class Messages(
    @Json(name="Id") var id:Int,
    @Json(name="SendUserId") var sendUserId:Int,
    @Json(name="ReceiverUserId") var takeUserId:Int,
    @Json(name="MessageContent") var maessageContent:String,
    @Json(name="Date") var Date:Date,
    @Json(name="SendUser") var users:Users,
)
