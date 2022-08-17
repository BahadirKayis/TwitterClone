package com.bhdr.twitterclone.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bhdr.twitterclone.helperclasses.DataItem
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.TweetsDatabase

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
   private val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
   private val tweetRepository = TweetRepository(dao!!, application)


   fun notificationTweet(): LiveData<List<DataItem.NotificationTweet>> {
      return tweetRepository.notificationTweet()
   }

   fun notificationLike(): LiveData<List<DataItem.NotificationLike>> {
      return tweetRepository.notificationLike()
   }
}
