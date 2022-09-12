package com.bhdr.twitterclone.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.common.hubConnection
import com.bhdr.twitterclone.common.toStartSignalRTweet
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.microsoft.signalr.HubConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TweetViewModel @Inject constructor(
   private val tweetRepositoryImpl: TweetRepository,
   @Named("IO") private val coContextIO: CoroutineDispatcher
) :
   ViewModel() {
   //room
   private val allRoomTweetsM = MutableLiveData<List<TweetsRoomModel>?>()
   val allRoomTweets: LiveData<List<TweetsRoomModel>?>
      get() = allRoomTweetsM


   //Insert işlemi için
   private val tweetsM = MutableLiveData<List<Posts>?>()
   val tweets: LiveData<List<Posts>?>
      get() = tweetsM

   var mainStatusM = MutableLiveData<Status>()
   val mainStatusL: LiveData<Status>
      get() = mainStatusM


   init {
      mainStatusM.value = Status.LOADING
      viewModelScope.launch {
         allRoomTweetsM.value = tweetRepositoryImpl.getTweetsRoom()
         if (!toStartSignalRTweet) {
            toStartSignalRTweet = true
            delay(5000)
            tweetSignalR()
         }

      }
      mainStatusM.value = Status.DONE

   }

   //Kullanıcı tweet ekranındayken yeni tweet gelirse gelenleri buraya alıyorum
   var mutableFollowNewTweetSignalR = MutableLiveData<HashMap<Int, String>>()
   val mutableFollowNewTweet: LiveData<HashMap<Int, String>> =
      mutableFollowNewTweetSignalR


   //BU da getTweets istek attığında yeni tweet varsa Tweet atanların id sini ve resmini getiriyor
   private var hashMapFollowNewTweetHashMap: HashMap<Int, String> = HashMap()
   private var mutableFollowNewTweetHashMapM = MutableLiveData<HashMap<Int, String>>()
   val mutableFollowNewTweetHashMapL: LiveData<HashMap<Int, String>>
      get() = mutableFollowNewTweetHashMapM

   fun getTweets(id: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.getTweets(id).also { it -> isNewTweet(it!!, allRoomTweetsM.value) }


      }

   }

   fun postLiked(id: Int, count: Int, userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.tweetLiked(id, count, userId)

      }
   }

   fun tweetsRoomConvertAndAdd(ks: List<Posts>) {
      viewModelScope.launch {
         mainStatusM.value = Status.LOADING
         tweetRepositoryImpl.tweetsRoomConvertAndAdd(ks).also { tweet -> tweetsInsert(tweet) }
         mainStatusM.value = Status.DONE
      }
   }

   fun getFollowedUserIdList(userId: Int) {
      viewModelScope.launch {
         tweetRepositoryImpl.getFollowedUserIdList(userId)
      }
   }

   private suspend fun isNewTweet(cloudTweet: List<Posts>, roomTweet: List<TweetsRoomModel>?) {
      mainStatusM.value = Status.LOADING
      tweetRepositoryImpl.isNewTweet(cloudTweet, roomTweet)
         .also { pair -> newTweetButton(pair.first, pair.second) }

   }


   private suspend fun newTweetButton(addTweet: List<Posts>?, updateTweet: List<Posts>?) {
      try {
         if (updateTweet != null) {
            tweetsUpdate(updateTweet)
         }
         if (hashMapFollowNewTweetHashMap.size != 0) {
            mutableFollowNewTweetHashMapM.value = hashMapFollowNewTweetHashMap
            tweetsM.value = addTweet
         } else {
            if (addTweet != null) {
               tweetsRoomConvertAndAdd(addTweet)
            }
         }

      } catch (e: Exception) {
         Log.e("newTweetButton-Ex", e.toString())
      }
      mainStatusM.value = Status.DONE
   }

   private suspend fun tweetsUpdate(tweets: List<Posts>) {
      allRoomTweetsM.value = tweetRepositoryImpl.tweetsUpdate(tweets)
   }

   private var job: Job? = null
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
               { id, imageUrl, name ->
                  Log.e("id", id.toString())
                  Log.e("imageUrl", imageUrl.toString())
                  Log.e("imageUrl", name.toString())
                  try {
                     job = CoroutineScope(coContextIO).launch {
                        CoroutineScope(Dispatchers.Main).launch {
                           mutableFollowNewTweetSignalR.value = tweetRepositoryImpl.signalRControl(
                              id.toInt(),
                              imageUrl,
                           )
                        }
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

   private suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>) {
      allRoomTweetsM.value = tweetRepositoryImpl.tweetsInsert(tweets)


   }
}
