package com.bhdr.twitterclone.data.repos

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.common.hubConnection
import com.bhdr.twitterclone.common.toLongDate
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.microsoft.signalr.HubConnectionState
import com.mikepenz.materialdrawer.util.ifNotNull
import kotlinx.coroutines.*
import java.io.File


class TweetRepositoryImpl(
   private val localSource: LocalDataSource,
   private val application: Application,
   private val remoteSource: RemoteDataSourceMain,
) : TweetRepository {

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<Status>()

   val tweets = MutableLiveData<List<Posts>?>()

   val tweetsRoomList = MutableLiveData<List<TweetsRoomModel>?>()

   val tweetAdded = MutableLiveData<Boolean>()

   private lateinit var imageUri: String
   private var haveImageUri: Boolean = false

   val mutableFollowNewTweetHashMap = MutableLiveData<HashMap<Int, String>>()

   private var hashMapFollowNewTweetHashMap: HashMap<Int, String> = HashMap()

   //SignalR
   private val job = Job()
   private val coroutineContext = Dispatchers.IO + job
   private val cIoScope = CoroutineScope(coroutineContext)

   private var followedUserIdList: List<Int>? = null

   var mutableFollowNewTweet: MutableLiveData<HashMap<Int, String>> = MutableLiveData()

   var mutableNotificationList = MutableLiveData<List<Any>>()

   override suspend fun getTweets(id: Int) {
      try {

         val request = remoteSource.getTweets(id)
         if (request.isSuccessful) {

            tweets.value = request.body()!!
            isNewTweet(request.body()!!, tweetsRoomList.value)
            Log.e("TweetRepoGetPosts", request.body().toString())

         } else if (!request.isSuccessful) {
            mainStatus.value = Status.ERROR
         }

      } catch (e: Exception) {
         Log.e("getTweets-Ex", e.toString())

         mainStatus.value = Status.ERROR
         if (e.message == "timeout") {
            getTweets(id)
         }
      }

   }

   override suspend fun tweetLiked(id: Int, count: Int, userId: Int) {
      try {
         val response = remoteSource.postLiked(userId, id, count)

         if (response.isSuccessful) {
            if (count == 1) {
               localSource.tweetIsLiked(id, response.body()!!, true)

            } else {
               localSource.tweetIsLiked(id, response.body()!!, false)
            }
         }

      } catch (e: Exception) {
         Log.e("tweetLikedEx", e.toString())
      }
   }

   override suspend fun addTweet(
      id: Int,
      tweetText: String,
      tweetImageName: String,
      tweetImage: Uri?
   ) {
      mainStatus.value = Status.LOADING
      try {
         val reference = db.reference
         if (tweetImage != null) {

            val imagesReference = reference.child("tweetpictures").child(tweetImageName)
            imagesReference.putFile(tweetImage).addOnSuccessListener {

               val uploadedPictureReference =
                  db.reference.child("tweetpictures").child(tweetImageName)
               uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                  val tweetPictureUrl = uri.toString()

                  runBlocking {
                     val addTweetResult =
                        remoteSource.addTweet(
                           id, tweetText, tweetPictureUrl,
                           toLongDate().toString()
                        )
                     if (addTweetResult.isSuccessful) {
                        mainStatus.value = Status.DONE
                        tweetAdded.value = addTweetResult.body()
                     } else if (!addTweetResult.isSuccessful) {
                        mainStatus.value = Status.ERROR
                        tweetAdded.value = false
                     }
                     Log.e("AddTweet-errorBody", addTweetResult.errorBody().toString())
                  }

               }
            }
         } else {
            val addTweetResult =
               remoteSource.addTweet(
                  id,
                  tweetText,
                  "null",
                  toLongDate().toString()
               )
            if (addTweetResult.isSuccessful) {
               mainStatus.value = Status.DONE
               tweetAdded.value = addTweetResult.body()
            } else if (!addTweetResult.isSuccessful) {
               mainStatus.value = Status.ERROR
               tweetAdded.value = false

            }

         }
      } catch (e: Exception) {
         mainStatus.value = Status.ERROR
         tweetAdded.value = false
         Log.e("RepositoryAdT-EX", e.toString())
      }

   }

   //room
   override suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>) {
      try {
         localSource.addTweet(tweets)
         delay(500)
         getTweetsRoom()
      } catch (e: Exception) {

         Log.e("tweetsInsert-Ex", e.toString())
      }
   }

   override suspend fun tweetsUpdate(tweets: List<Posts>) {
      tweets.forEach {
         localSource.updateTweet(it.id!!, it.postLike!!)
      }
      getTweetsRoom()
   }

   override suspend fun getTweetsRoom() {
      mainStatus.value = Status.LOADING
      val tweetsRoomModelList = localSource.allTweet()
      Log.i("tweetsRoomModelList", tweetsRoomModelList.toString())
      if (tweetsRoomModelList != null) {
         tweetsRoomList.value = tweetsRoomModelList

         mainStatus.value = Status.DONE
      } else {
         tweetsRoomList.value = null

      }
   }

   override suspend fun tweetsRoomConvertAndAdd(lTweet: List<Posts?>) {

      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         lTweet.forEach {
            var userRoomModel: UsersRoomModel? = null
            imageUri =
               createImageUri(it!!.userId.toString() + "_" + it.user?.userName + "_user_profile.png")
            if (!haveImageUri) {
               val bmp = getBitmap(it.user!!.photoUrl!!)//fotoğraf indiriliyor
               bmp.ifNotNull { storeBitmap(bmp) }//fotoğraf kayıt ediliyior
            }
            it.user?.apply {
               userRoomModel =
                  UsersRoomModel(
                     id,
                     imageUri,
                     userName,
                     name
                  )
            }
            it.apply {
               if (it.postImageUrl != "null") {
                  imageUri =
                     createImageUri(id.toString() + "_" + userId.toString() + "_tweet_content.png")
                  if (!haveImageUri) {
                     val bmp = getBitmap(it.postImageUrl.toString())//fotoğraf indiriliyor
                     bmp.ifNotNull { storeBitmap(bmp) }//fotoğraf kayıt ediliyor
                  }
               } else {
                  imageUri = "null"
               }
               tweetsAddRoom.add(
                  TweetsRoomModel(
                     date,
                     id,
                     postContent,
                     imageUri,
                     postLike,
                     userRoomModel,
                     userId
                  )
               )
            }
            tweetsInsert(tweetsAddRoom)
         }
         mainStatus.value = Status.DONE

      } catch (e: Exception) {
         Log.e("RoomConvertAndAdd-Ex", e.toString())
      }
   }

   override suspend fun getBitmap(url: String): Bitmap {
      try {
         val loading = ImageLoader(application)
         val request = ImageRequest.Builder(application)
            .data(url)
            .build()
         val result = (loading.execute(request) as SuccessResult).drawable
         return (result as BitmapDrawable).bitmap
      } catch (e: Exception) {
         Log.e("getBitmap-Ex", e.toString())
         throw  e
      }

   }

   override fun createImageUri(photoName: String): String {
      val image = File(application.filesDir, photoName)
      haveImageUri = image.isFile

      return FileProvider.getUriForFile(
         application,
         "com.bhdr.twitterclone.fileProvider",
         image
      ).toString()
   }

   override fun storeBitmap(bmp: Bitmap) {
      val outputStream =
         application.contentResolver.openOutputStream(Uri.parse(imageUri))
      bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      outputStream!!.close()
   }


   override suspend fun isNewTweet(cloudTweet: List<Posts>, roomTweet: List<TweetsRoomModel>?) {

      val tweetRoomUpdateList: MutableList<Posts> = mutableListOf()
      val tweetCloudAddToRoomList: MutableList<Posts> = mutableListOf()
      try {
         if (roomTweet?.size == 0) {
            newTweetButton(cloudTweet, tweetRoomUpdateList)
         } else {
            if (cloudTweet.isNotEmpty()) {
               if (roomTweet!!.size != cloudTweet.size) {
                  roomTweet.forEach { itCloud ->
                     val tweet: Posts? = cloudTweet.find { itRoom ->
                        itCloud.id != itRoom.id
                     }
                     if (tweet != null) {
                        hashMapFollowNewTweetHashMap[tweet.userId!!] =
                           tweet.user?.photoUrl.toString()
                        tweetCloudAddToRoomList.add(tweet)

                     } else {
                        tweetRoomUpdateList.add(cloudTweet[itCloud.id!!])
                     }
                  }
                  newTweetButton(tweetCloudAddToRoomList, tweetRoomUpdateList)
               } else {
                  newTweetButton(null, cloudTweet)
               }
            }
         }
      } catch (e: Exception) {
         Log.e("isNewTweet-Ex", e.toString())
      }
   }

   override suspend fun newTweetButton(addTweet: List<Posts>?, updateTweet: List<Posts>?) {
      try {
         if (updateTweet != null) {
            tweetsUpdate(updateTweet)
         }
         if (hashMapFollowNewTweetHashMap.size != 0) {
            mutableFollowNewTweetHashMap.value = hashMapFollowNewTweetHashMap
            tweets.value = addTweet
         } else {
            if (addTweet != null) {
               tweetsRoomConvertAndAdd(addTweet)
            }
         }

      } catch (e: Exception) {
         Log.e("newTweetButton-Ex", e.toString())
      }
   }

   ////////////////
   override suspend fun getFollowedUserIdList(userId: Int) {
      val response = remoteSource.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
      Log.i("getFollowedUserIdList", response.body().toString())
   }

   override fun tweetSignalR() {
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
                     cIoScope.launch {
                        signalRControl(
                           id.toInt(),
                           imageUrl,
                        )
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

   override fun signalRControl(
      id: Int,
      imageUrl: String
   ) {
      try {
         if (id != 0) {
            //NewTweet follow
            cIoScope.launch {
               CoroutineScope(Dispatchers.Main).launch {

                  val userId = followedUserIdList?.find { it == id }

                  if (userId != null) {
                     hashMapFollowNewTweetHashMap[id] = imageUrl
                     mutableFollowNewTweet.value = hashMapFollowNewTweetHashMap
                  }
               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControl-Ex", e.toString())
      }
   }

   //NotificationScreen

   override suspend fun notificationList() {
      val tweetList: MutableList<Any> = mutableListOf()
      tweetList.addAll(localSource.notificationListLike())
      tweetList.addAll(localSource.notificationListTweet())
      mutableNotificationList.value = tweetList
   }
}

