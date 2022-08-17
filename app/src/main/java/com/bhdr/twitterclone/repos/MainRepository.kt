package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.DataItem
import com.bhdr.twitterclone.helperclasses.hubConnection
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetDaoInterface
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MainRepository(private val tweetDao: TweetDaoInterface) {
   var followCount = MutableLiveData<Int>()

   var followedCount = MutableLiveData<Int>()

   private var followedUserIdList: List<Int>? = null

   var notificationCount = MutableLiveData<Int>()


   private val job = Job()
   private val coroutineContext = Dispatchers.IO + job
   private val caScope = CoroutineScope(coroutineContext)

   suspend fun followCount(userId: Int) {
      try {
         val response = CallApi.retrofitServiceMain.getFollowCount(userId)

         if (response.isSuccessful) {
            followCount.postValue(response.body())
         }

      } catch (e: Exception) {
         Log.e("Ex", e.toString())

      }
   }

   suspend fun followedCount(userId: Int) {
      val response = CallApi.retrofitServiceMain.getFollowedCount(userId)

      if (response.isSuccessful) {
         followedCount.postValue(response.body())
      }

   }


   suspend fun getFollowedUserIdList(userId: Int) {
      val response = CallApi.retrofitServiceMain.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
      Log.e("response", response.body().toString())
   }


   fun tweetSignalR(userId: Int) {

      try {

         if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()
         }
         //NewTweet follow & not follow
         try {
            hubConnection.on(
               "newTweets",
               { id, imageUrl, userName, name, post ->
                  Log.e("id", id.toString())
                  Log.e("imageUrl", imageUrl.toString())
                  Log.e("imageUrl", name.toString())
                  try {
                     caScope.launch {
                        signalRControl(
                           id.toInt(),
                           imageUrl,
                           userName,
                           name,
                           post.toInt(),
                           null
                        )
                     }

                  } catch (e: Throwable) {

                     Log.e("imageUrl", e.toString())
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

//Like imagUrl.PhotoUrl,imagUrl.UserName,imagUrl.Name,post
         hubConnection.on(
            userId.toString(), { imageUrl, userName, name, post ->
               caScope.launch {
                  signalRControl(0, imageUrl, userName, name, 0, post)
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

   private suspend fun tweetNew(tweetId: Int): Posts {
      val response = CallApi.retrofitServiceMain.getTweetNew(tweetId)
      return response.body() as Posts
   }

   private suspend fun signalRControl(
      id: Int,
      imageUrl: String,
      userName: String,
      name: String,
      postId: Int,
      post: Posts?
   ) {
      try {
         caScope.launch {
            CoroutineScope(Dispatchers.Main).launch {

               if (id == 0) {
                  //Like tweeti beğinilmiş modele atılacak

                  saveNotificationLike(
                     DataItem.NotificationLike(
                        id,
                        imageUrl,
                        userName,
                        name,
                        Calendar.getInstance().time.toString(),
                        tweetsRoomConvertAndAdd(post!!)

                     )
                  )
                  notificationCount.value = notificationCount.value?.toInt()?.plus(1)

               } else {

                  //NewTweet follow & not follow
                  val haveId = followedUserIdList?.find { it == id }
                  if (haveId == null) {
                     //Takipe etmiyor bildirim ekranında gösterilecek modelde olacak
                     val getPost: Posts = tweetNew(postId)

                     saveNotificationTweet(
                        DataItem.NotificationTweet(
                           id,
                           imageUrl,
                           userName,
                           name,
                           Calendar.getInstance().time.toString(),
                           tweetsRoomConvertAndAdd(getPost)
                        )
                     )
                     notificationCount.value = notificationCount.value?.toInt()?.plus(1)
                  }
               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControlEx", e.toString())
      }

   }

   private suspend fun saveNotificationTweet(it: DataItem.NotificationTweet) =
      tweetDao.addNotificationTweet(it)

   private suspend fun saveNotificationLike(it: DataItem.NotificationLike) =
      tweetDao.addNotificationLike(it)

   private fun tweetsRoomConvertAndAdd(tweet: Posts): TweetsRoomModel {
      try {
         var userRoomModel: UsersRoomModel? = null
         tweet.user?.apply {
            userRoomModel =
               UsersRoomModel(
                  id,
                  photoUrl!!,
                  userName,
                  name
               )
         }
         tweet.apply {
            return TweetsRoomModel(
               date,
               id,
               postContent,
               postImageUrl,
               postLike,
               userRoomModel,
               userId
            )
         }
      } catch (e: Exception) {
         Log.e("tweetsRoomConvertAndAdd", e.toString())
         throw IllegalStateException("tweetsRoomConvertAndAdd")
      }

   }
}