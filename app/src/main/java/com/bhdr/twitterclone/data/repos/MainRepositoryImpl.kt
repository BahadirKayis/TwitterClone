package com.bhdr.twitterclone.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.common.hubConnection
import com.bhdr.twitterclone.common.toLongDate
import com.bhdr.twitterclone.data.model.locale.DataItem
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainRepositoryImpl constructor(
   private val tweetDao: LocalDataSource,
   private val remoteSourceMain: RemoteDataSourceMain,
   private val remoteSourceLogin: RemoteDataSourceLogin
) {

   var followCount = MutableLiveData<Int>()

   var followedCount = MutableLiveData<Int>()

   private var followedUserIdList: List<Int>? = null

   var notificationCount = MutableLiveData<Int>()


   private val job = Job()
   private val coroutineContext = Dispatchers.IO + job
   private val caScope = CoroutineScope(coroutineContext)

   val roomDelete = MutableLiveData<Boolean>()

   suspend fun followCount(userId: Int) {

      val response = remoteSourceMain.getFollowCount(userId)
      if (response.isSuccessful) {
         followCount.postValue(response.body())
      }

   }

   suspend fun followedCount(userId: Int) {
      val response = remoteSourceMain.getFollowedCount(userId)

      if (response.isSuccessful) {
         followedCount.postValue(response.body())
      }

   }


   suspend fun getFollowedUserIdList(userId: Int) {
      val response = remoteSourceMain.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
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
      val response = remoteSourceMain.getTweetNew(tweetId)
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
                  val rep = remoteSourceLogin.getUser(post?.userId!!)
                  post.user = rep.body()
                  //Like tweeti beğinilmiş modele atılacak
                  saveNotificationLike(
                     DataItem.NotificationLike(
                        null,
                        id,
                        imageUrl,
                        userName,
                        name,
                        toLongDate().toString(),
                        tweetsRoomConvertAndAdd(post)
                     )
                  )
                  if (notificationCount.value == null) {
                     notificationCount.value = 0
                  }
                  notificationCount.value = notificationCount.value?.toInt()?.plus(1)

               } else {
                  //NewTweet follow & not follow
                  val haveId = followedUserIdList?.find { it == id }
                  if (haveId == null) {
                     //Takipe etmiyor bildirim ekranında gösterilecek modelde olacak
                     val getPost: Posts = tweetNew(postId)

                     saveNotificationTweet(
                        DataItem.NotificationTweet(
                           null,
                           id,
                           imageUrl,
                           userName,
                           name,
                           toLongDate().toString(),
                           tweetsRoomConvertAndAdd(getPost)
                        )
                     )
                     if (notificationCount.value == null) {
                        notificationCount.value = 0
                     }
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
         Log.e("RoomConvertAndAdd-ExM", e.toString())
         throw IllegalStateException("tweetsRoomConvertAndAdd")
      }

   }

   suspend fun deleteAllRoom() {
      tweetDao.notificationDeleteLike()
      tweetDao.notificationDeleteTweet()
      val response = tweetDao.deleteTweet()
      roomDelete.value = response != null

   }

}