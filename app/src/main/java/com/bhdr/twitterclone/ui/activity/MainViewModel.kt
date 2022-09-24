package com.bhdr.twitterclone.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.bhdr.twitterclone.common.hubConnection
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.ActivityRepository
import com.microsoft.signalr.HubConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
   private val activityRepositoryImpl: ActivityRepository,
   @Named("IO") private val coContextMain: CoroutineDispatcher
) : ViewModel() {
   private var notificationCountM = MutableLiveData<Boolean>()
   val notificationCount: LiveData<Boolean> = notificationCountM

   private var followCountM = MutableLiveData<Int>()
   val followCount: LiveData<Int>
      get() = followCountM

   private var followedCountM = MutableLiveData<Int>()
   val followedCount: LiveData<Int>
      get() = followedCountM

   private var roomDeleteM = MutableLiveData<Boolean>()
   val roomDelete: LiveData<Boolean>
      get() = roomDeleteM


   fun followCount(userId: Int) {
      viewModelScope.launch {
         followCountM.value = activityRepositoryImpl.followCount(userId)
      }
   }

   fun followedCount(userId: Int) {
      viewModelScope.launch {
         followedCountM.value = activityRepositoryImpl.followedCount(userId)
      }
   }

   fun startSignalR(userId: Int) {
      tweetSignalR(userId)
   }

   fun getFollowedUserIdList(id: Int) {
      viewModelScope.launch {
         activityRepositoryImpl.getFollowedUserIdList(id)
      }
   }

   fun roomDelete() {
      viewModelScope.launch {
         roomDeleteM.value = activityRepositoryImpl.deleteAllRoom()
      }
   }


   private var job: Job? = null
   private fun tweetSignalR(userId: Int) {
      try {

         if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()
         }
         //NewTweet follow & not follow
         try {
            hubConnection.on(
               "newTweets",
               { id, imageUrl, userName, name, post ->
                  try {
                     job = CoroutineScope(coContextMain).launch {
                        activityRepositoryImpl.signalRControl(
                           id.toInt(),
                           imageUrl,
                           userName,
                           name,
                           post.toInt(),
                           null
                        ) { notificationCountM.postValue(it) }
                     }

                  } catch (e: Throwable) {

                     Log.e("newTweets-EX", e.toString())
                  }
               },
               String::class.java,
               String::class.java,
               String::class.java,
               String::class.java,
               String::class.java
            )

         } catch (e: Throwable) {

            Log.e("newTweets", e.toString())
         }

         hubConnection.on(
            userId.toString(), { imageUrl, userName, name, post ->
               job = CoroutineScope(coContextMain).launch {
                  activityRepositoryImpl.signalRControl(
                     0,
                     imageUrl,
                     userName,
                     name,
                     0,
                     post
                  ) { notificationCountM.postValue(it) }

               }
            }, String::class.java,
            String::class.java,
            String::class.java,
            Posts::class.java
         )
      } catch (e: Exception) {
         Log.e("tweetSignalRException", e.toString())
      }
   }
}