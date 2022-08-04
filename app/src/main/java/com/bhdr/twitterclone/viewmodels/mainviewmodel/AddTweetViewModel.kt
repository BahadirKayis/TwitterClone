package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.repos.TweetRepository
import kotlinx.coroutines.launch

class AddTweetViewModel : ViewModel() {
   private var mainrepo = TweetRepository()
   val tweetAdded: LiveData<Boolean> = mainrepo.tweetAdded
   val mainStatus: LiveData<TweetRepository.MainStatus> = mainrepo.mainStatus
   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      viewModelScope.launch {
         mainrepo.addTweet(id, tweetText, tweetImageName, tweetImage)
      }

   }
}