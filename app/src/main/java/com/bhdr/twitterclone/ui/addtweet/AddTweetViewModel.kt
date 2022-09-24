package com.bhdr.twitterclone.ui.addtweet

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.domain.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTweetViewModel @Inject constructor(private val tweetRepositoryImpl: TweetRepository) :
   ViewModel() {

   private var tweetAddedM = MutableLiveData<Boolean>()
   val tweetAdded: LiveData<Boolean>
      get() = tweetAddedM

   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      viewModelScope.launch {
         tweetRepositoryImpl.addTweet(
            id,
            tweetText,
            tweetImageName,
            tweetImage
         ) { tweetAddedM.value = it }
      }
   }
}
