package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.TweetRepository
import com.bhdr.twitterclone.room.TweetsDatabase
import kotlinx.coroutines.launch

class AddTweetViewModel(application: Application) : AndroidViewModel(application) {
   //Sorulacak ayrı bir repoyamı taşuınsa daha iyi olur yoksa böyle okey mi
   private val tweetRepository: TweetRepository
   val tweetAdded: LiveData<Boolean>
      get() =
         tweetRepository.tweetAdded
   val mainStatus: LiveData<TweetRepository.MainStatus>
      get() =
         tweetRepository.mainStatus

   init {
      val dao = TweetsDatabase.getTweetsDatabase(application)?.tweetDao()
      tweetRepository = TweetRepository(dao!!, application)

   }

   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      viewModelScope.launch {
         tweetRepository.addTweet(id, tweetText, tweetImageName, tweetImage)
      }

   }
}