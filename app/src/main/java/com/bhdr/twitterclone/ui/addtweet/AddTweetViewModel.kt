package com.bhdr.twitterclone.ui.addtweet

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.domain.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTweetViewModel @Inject constructor(private val tweetRepositoryImpl: TweetRepository) :
   ViewModel() {

   private val tweetAddedM = MutableLiveData<Boolean>()
   val tweetAdded: LiveData<Boolean>
      get() = tweetAddedM

   private var mainStatusM = MutableLiveData<Status>()
   val mainStatus: LiveData<Status>
      get() = mainStatusM

   fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      mainStatusM.value = Status.LOADING
      viewModelScope.launch {
         tweetRepositoryImpl.addTweet(id, tweetText, tweetImageName, tweetImage).let {
            tweetAddedM.value = it
            when (it) {
               true -> {
                  mainStatusM.value = Status.DONE
               }
               false -> mainStatusM.value = Status.ERROR

            }
         }
      }
   }
}