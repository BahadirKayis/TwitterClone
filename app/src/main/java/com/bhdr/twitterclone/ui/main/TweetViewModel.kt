package com.bhdr.twitterclone.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.common.checkNetworkConnection
import com.bhdr.twitterclone.common.hubConnection
import com.bhdr.twitterclone.common.toStartSignalRTweet
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.microsoft.signalr.HubConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TweetViewModel @Inject constructor(
   private val tweetRepositoryImpl: TweetRepository,
   private val application: Application,
   @Named("IO") private val coContextIO: CoroutineDispatcher
) :
   ViewModel() {
   private var job: Job? = null

   private val allRoomTweetsM = MutableLiveData<List<TweetsRoomModel>?>()
   val allRoomTweets: LiveData<List<TweetsRoomModel>?>
      get() = allRoomTweetsM


   //For Insert
   private val tweetsM = MutableLiveData<List<Posts>?>()
   val tweets: LiveData<List<Posts>?>
      get() = tweetsM

   private var mainStatusM = MutableLiveData<Status>()
   val mainStatusL: LiveData<Status>
      get() = mainStatusM

   init {
      viewModelScope.launch {
         mainStatusM.value = Status.LOADING
         tweetRepositoryImpl.getTweetsRoom().let {
            allRoomTweetsM.value = it
            if (it != null) {
               mainStatusM.value = Status.DONE
            } else {
               mainStatusM.value = Status.LOADING
            }
         }
         if (application.checkNetworkConnection()) {
            if (!toStartSignalRTweet) {
               toStartSignalRTweet = true
               tweetSignalR()
            }
         }

      }

   }

   private var mutableFollowNewTweetHashMapM = MutableLiveData<HashMap<Int, String>?>()
   val followNewTweetList: LiveData<HashMap<Int, String>?>
      get() = mutableFollowNewTweetHashMapM

   val followNewTweetListSignalR: LiveData<HashMap<Int, String>>
      get() = tweetRepositoryImpl.hashMapNewTweet()

   fun getTweets(id: Int) {
      viewModelScope.launch {
         isNewTweet(tweetRepositoryImpl.getTweets(id)!!, allRoomTweetsM.value)
      }

   }

   private fun isNewTweet(cloudTweet: List<Posts>, roomTweet: List<TweetsRoomModel>?) {
      viewModelScope.launch {
         tweetRepositoryImpl.isNewTweet(cloudTweet, roomTweet)
            .let { triple -> newTweetButton(triple.first, triple.second, triple.third) }

      }
   }

   private suspend fun newTweetButton(
      addTweet: List<Posts>?,
      updateTweet: List<Posts>?,
      newTweetButton: HashMap<Int, String>?
   ) {
      try {
         if (updateTweet != null) {

            tweetsUpdate(updateTweet)
         }

         if (newTweetButton != null) {

            mutableFollowNewTweetHashMapM.value = newTweetButton
            tweetsM.value = addTweet
         } else {
            if (addTweet != null) {
               tweetsRoomConvertAndAdd(addTweet)
            }
         }
      } catch (e: Exception) {
         Log.e("newTweetButton-Ex", e.toString())
      }
   }


   private suspend fun tweetsUpdate(tweets: List<Posts>) {
      allRoomTweetsM.value = tweetRepositoryImpl.tweetsUpdate(tweets)
   }

   fun tweetsRoomConvertAndAdd(ks: List<Posts>) {
      mainStatusM.value = Status.LOADING
      viewModelScope.launch {
         tweetRepositoryImpl.tweetsRoomConvertAndAdd(ks)
            ?.let { tweetsInsert(it);mainStatusM.value = Status.DONE }
      }

   }

   private suspend fun tweetsInsert(tweets: List<TweetsRoomModel>) {

      allRoomTweetsM.value = tweetRepositoryImpl.tweetsInsert(tweets)
   }

   fun postLiked(id: Int, count: Int, userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.tweetLiked(id, count, userId)

      }
   }

   fun getFollowedUserIdList(userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.getFollowedUserIdList(userId)
      }
   }

   private fun tweetSignalR() {
      try {
         if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()
         }
         Log.i("tweetSignalR", hubConnection.connectionState.toString())
         //NewTweet follow & not follow
         try {
            hubConnection.on(
               "newTweets",
               { id, imageUrl, _ ->

                  try {
                     job = CoroutineScope(coContextIO).launch {


                        tweetRepositoryImpl.signalRControl(id.toInt(), imageUrl)
                     }

                  } catch (e: Throwable) {

                     Log.e("newTweets-Ex", e.toString())
                  }
               },
               String::class.java,
               String::class.java,
               String::class.java

            )

         } catch (e: Throwable) {

            Log.e("newTweets", e.toString())
         }


      } catch (e: Exception) {
         Log.e("tweetSignalR-Ex", e.toString())
      }
   }

}
