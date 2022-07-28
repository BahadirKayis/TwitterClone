package com.bhdr.twitterclone.viewmodels.mainviewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.SignalRModel
import com.bhdr.twitterclone.repos.TweetRepository

import kotlinx.coroutines.launch

class TweetViewModel : ViewModel() {
   private var mainrepo = TweetRepository()
   val tweets: LiveData<List<Posts>> = mainrepo.tweets
   val mainStatus: LiveData<TweetRepository.MainStatus> = mainrepo.mainStatus
   val tweetAdded: LiveData<Boolean> = mainrepo.tweetAdded
   val liked: LiveData<Int> = mainrepo.liked
   fun getPosts(id: Int) {
      viewModelScope.launch {
         mainrepo.getPosts(id)
      }

   }


   fun postLiked(id: Int, count: Int, context: Context) {
      viewModelScope.launch {
         mainrepo.postLiked(id, count, context)

      }
   }

   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      viewModelScope.launch {
         mainrepo.addTweet(id, tweetText, tweetImageName, tweetImage)
      }

   }



}