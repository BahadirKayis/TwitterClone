package com.bhdr.twitterclone.data.repos

import android.util.Log
import com.bhdr.twitterclone.common.toLongDate
import com.bhdr.twitterclone.data.model.locale.DataItem
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.MainRepository
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import kotlinx.coroutines.*
import javax.inject.Named

class MainRepositoryImpl(
   private val tweetDao: LocalDataSource,
   private val remoteSourceMain: RemoteDataSourceMain,
   @Named("IO") private val coContextIO: CoroutineDispatcher
) : MainRepository {

   private var followedUserIdList: List<Int>? = null

   private var notificationCount: Int = 0

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
      post: Posts?
   ): Int {
      try {
         job = CoroutineScope(coContextIO).launch {
            CoroutineScope(Dispatchers.Main).launch {
               if (id == 0) {
                  val rep = remoteSourceMain.getUser(post?.userId!!)
                  Log.e("TAG", rep.headers().toString())
                  Log.e("TAG", rep.body().toString())
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

                  notificationCount.plus(1)

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

                     notificationCount.plus(1)
                  }
               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControlEx", e.toString())
      }
      return notificationCount
   }

   override suspend fun deleteAllRoom(): Boolean {
      tweetDao.notificationDeleteLike()
      tweetDao.notificationDeleteTweet()
      return tweetDao.deleteTweet() != null
   }


   private suspend fun saveNotificationTweet(it: DataItem.NotificationTweet) =
      tweetDao.addNotificationTweet(it)

   private suspend fun saveNotificationLike(it: DataItem.NotificationLike) =
      tweetDao.addNotificationLike(it)


}