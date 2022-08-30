package com.bhdr.twitterclone.data.model.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "signalRModel")
data class SignalRModel(
   @PrimaryKey(autoGenerate = true)
   var id: Int,
   var imageUrl: String,
   var userName: String,
   var name: String,
   var isLiked: Boolean,
   var date: Date,
   val tweet: Posts
)
