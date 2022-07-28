package com.bhdr.twitterclone.models

data class SignalRModel(
   var id: Int,
   var imageUrl: String,
   var userName: String,
   var name: String,
   val post: Posts
)
