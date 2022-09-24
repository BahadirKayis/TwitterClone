package com.bhdr.twitterclone.data.repos

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.common.toLongDate
import com.bhdr.twitterclone.common.userId
import com.bhdr.twitterclone.data.model.locale.NotificationsDataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.ActivityRepository
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import kotlinx.coroutines.*
import javax.inject.Named

class ActivityRepositoryImpl(
   private val localDataSource: LocalDataSource,
   private val remoteSourceMain: RemoteDataSourceMain,
   @Named("IO") private val coContextIO: CoroutineDispatcher,
   private val application: Application
) : ActivityRepository {

   private var followedUserIdList: List<Int>? = null
   private var job: Job? = null


   override suspend fun followCount(userId: Int): Int? =
      remoteSourceMain.getFollowCount(userId).body()

   override suspend fun followedCount(userId: Int): Int? =
      remoteSourceMain.getFollowedCount(userId).body()


   override suspend fun getFollowedUserIdList(userId: Int) {
      val response = remoteSourceMain.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
   }


   override fun tweetsRoomConvertAndAdd(tweet: Posts): TweetsRoomModel {
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

   override suspend fun tweetNew(tweetId: Int): Posts {
      val response = remoteSourceMain.getTweetNew(tweetId)
      return response.body() as Posts
   }

   override suspend fun signalRControl(
      id: Int,
      imageUrl: String,
      userName: String,
      name: String,
      postId: Int,
      post: Posts?,result: (Boolean) -> Unit
   ) {
      try {
         job = CoroutineScope(coContextIO).launch {
            CoroutineScope(Dispatchers.Main).launch {
               if (id == 0) {
                  val rep = remoteSourceMain.getUser(post?.userId!!)
                  post.user = rep.body()
                  //Like tweeti beğinilmiş modele atılacak
                  //tweet önceden locale kayıtlı ise tekrar kayıt etmeyecek
                  if (!isTweet(id, post.id!!)) {
                     saveNotificationLike(
                        NotificationsDataItem.NotificationLike(
                           null,
                           id,
                           imageUrl,
                           userName,
                           name,
                           toLongDate().toString(),
                           tweetsRoomConvertAndAdd(post)
                        )
                     )
                  }
               result(true)


               } else {
                  //NewTweet follow & not follow
                  val haveId = followedUserIdList?.find { it == id }

                  if (haveId == null && application.userId() != id) {
                     //Takipe etmiyor bildirim ekranında gösterilecek modelde olacak
                     val getPost: Posts = tweetNew(postId)
                     saveNotificationTweet(
                        NotificationsDataItem.NotificationTweet(
                           null,
                           id,
                           imageUrl,
                           userName,
                           name,
                           toLongDate().toString(),
                           tweetsRoomConvertAndAdd(getPost)
                        )
                     )
                     result(true)
                  }
               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControlEx", e.toString())
      }

   }

   override suspend fun deleteAllRoom(): Boolean {
      localDataSource.notificationDeleteLike()
      localDataSource.notificationDeleteTweet()
      return localDataSource.deleteTweet() != null
   }


   private suspend fun saveNotificationTweet(it: NotificationsDataItem.NotificationTweet) =
      localDataSource.addNotificationTweet(it)

   private suspend fun saveNotificationLike(it: NotificationsDataItem.NotificationLike) =
      localDataSource.addNotificationLike(it)

   private suspend fun isTweet(userId: Int, tweetId: Int): Boolean {
      val list = localDataSource.isTweetQuery(userId)
      if (list != null) {
         val roomTweetObjectIds = list.map { it.tweet!!.id }.toSet()
         return roomTweetObjectIds.contains(tweetId)
      }
      return false
   }
}