package com.bhdr.twitterclone.ui.addtweet

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.repos.TweetRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTweetViewModel @Inject constructor(private val tweetRepositoryImpl: TweetRepositoryImpl) :
   ViewModel() {


   val tweetAdded: LiveData<Boolean>
      get() =
         tweetRepositoryImpl.tweetAdded
   val mainStatus: LiveData<Status>
      get() =
         tweetRepositoryImpl.mainStatus

   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      viewModelScope.launch {
         tweetRepositoryImpl.addTweet(id, tweetText, tweetImageName, tweetImage)
      }

   }
}