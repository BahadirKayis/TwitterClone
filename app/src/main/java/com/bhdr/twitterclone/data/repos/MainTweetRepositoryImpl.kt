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
import com.bhdr.twitterclone.common.toLongDate
import com.bhdr.twitterclone.data.model.locale.TweetsRoomModel
import com.bhdr.twitterclone.data.model.locale.UsersRoomModel
import com.bhdr.twitterclone.data.model.remote.Posts
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File


class MainTweetRepositoryImpl(
   private val localSource: LocalDataSource,
   private val application: Application,
   private val remoteSource: RemoteDataSourceMain,
   private val coContextIO: CoroutineDispatcher,
   private val FirebaseStorage: FirebaseStorage
) : TweetRepository {


   private lateinit var imageUri: String
   private var haveImageUri: Boolean = false
   private var job: Job? = null
   private var followedUserIdList: List<Int>? = null


   private var followNewTweet: HashMap<Int, String> = HashMap()
   private var followNewTweetM = MutableLiveData<HashMap<Int, String>>()


   override suspend fun getTweets(id: Int): List<Posts>? = remoteSource.getTweets(id).body()

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
      tweetImage: Uri?, result: (Boolean) -> Unit
   ) {

      try {
         val reference = FirebaseStorage.reference
         if (tweetImage != null) {

            val imagesReference = reference.child("tweetpictures").child(tweetImageName)
            imagesReference.putFile(tweetImage).addOnSuccessListener {

               val uploadedPictureReference =
                  FirebaseStorage.reference.child("tweetpictures").child(tweetImageName)
               uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                  val tweetPictureUrl = uri.toString()
                  job = CoroutineScope(coContextIO).launch {
                     val addTweetResult =
                        remoteSource.addTweet(
                           id, tweetText, tweetPictureUrl,
                           toLongDate().toString()
                        )
                     result(addTweetResult.body()!!)
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
            result(addTweetResult.body()!!)

         }
      } catch (e: Exception) {
         result(false)
         Log.e("RepositoryAdT-EX", e.toString())
      }
   }


   override suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>): List<TweetsRoomModel>? {
      localSource.addTweet(tweets)
      return getTweetsRoom()
   }

   override suspend fun tweetsUpdate(tweets: List<Posts>): List<TweetsRoomModel>? {
      tweets.forEach {
         localSource.updateTweet(it.id!!, it.postLike!!)
      }
      return getTweetsRoom()
   }

   override suspend fun getTweetsRoom(): List<TweetsRoomModel>? = localSource.allTweet()


   override suspend fun tweetsRoomConvertAndAdd(
      lTweet: List<Posts?>
   ): List<TweetsRoomModel> {

      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         lTweet.forEach {
            var userRoomModel: UsersRoomModel?
            imageUri =
               createImageUri(it!!.userId.toString() + "_" + it.user?.userName + "_user_profile.png")
            if (!haveImageUri) {//yol var ise fotoğrafta var demek tekrar indirmiyor
               storeBitmap(getBitmap(it.user!!.photoUrl!!))
//               val bmp = getBitmap(it.user!!.photoUrl!!)//Image download
//               bmp.ifNotNull { }//Image save
            }
            with(it.user!!) {
               userRoomModel =
                  UsersRoomModel(
                     id,
                     imageUri,
                     userName,
                     name
                  )
            }
            with(it)
            {
               if (it.postImageUrl != "null") {

                  if (it.postImageUrl!!.contains("video")) {
                     imageUri = it.postImageUrl.toString()
                  } else {
                     imageUri =
                        createImageUri(id.toString() + "_" + userId.toString() + "_tweet_content.png")
                     storeBitmap(getBitmap(it.postImageUrl.toString()))
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
         }
         return (tweetsAddRoom)

      } catch (e: Exception) {
         Log.e("RoomConvertAndAdd-Ex", e.toString())
      }

      return (tweetsAddRoom)
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


   override suspend fun isNewTweet(
      cloudTweet: List<Posts>,
      roomTweet: List<TweetsRoomModel>?
   ): Triple<List<Posts>?, List<Posts>?, HashMap<Int, String>?> {

      val tweetRoomUpdateList: MutableList<Posts> = mutableListOf()
      val tweetCloudAddToRoomList: MutableList<Posts> = mutableListOf()

      try {
         if (roomTweet?.size == 0) {

            return Triple(cloudTweet, cloudTweet, null)
         } else {
            if (cloudTweet.isNotEmpty()) {
               if (roomTweet!!.size != cloudTweet.size) {
                  roomTweet.forEach { itCloud ->
                     val tweet: Posts? = cloudTweet.find { itRoom ->
                        itCloud.id != itRoom.id
                     }
                     if (tweet != null) {
                        followNewTweet[tweet.userId!!] =
                           tweet.user?.photoUrl.toString()
                        tweetCloudAddToRoomList.add(tweet)

                     } else {
                        tweetRoomUpdateList.add(cloudTweet[itCloud.id!!])
                     }
                     val roomTweetObjectIds = roomTweet.map { it.id }.toSet()
                     val addTweet = cloudTweet.filter { !roomTweetObjectIds.contains(it.id) }
                     val updateTweet = cloudTweet.filter { roomTweetObjectIds.contains(it.id) }

                     addTweet.forEach {
                        followNewTweet[it.userId!!] =
                           it.user?.photoUrl.toString()
                        tweetCloudAddToRoomList.add(it)
                     }
                     // newTweetButton(tweetCloudAddToRoomList, tweetRoomUpdateList)
                     updateTweet.forEach { tweetRoomUpdateList.add(it) }
                     return Triple(
                        tweetCloudAddToRoomList,
                        tweetRoomUpdateList,
                        followNewTweet
                     )
                  }
               }
            } else {
               //  newTweetButton(null, cloudTweet)
               return Triple(null, tweetRoomUpdateList, null)
            }
         }
      } catch (e: Exception) {
         Log.e("isNewTweet-Ex", e.toString())
      }

      return Triple(null, null, followNewTweet)


   }


   ////////////////
   override suspend fun getFollowedUserIdList(userId: Int) {
      val response = remoteSource.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
      Log.i("getFollowedUserIdList", response.body().toString())
   }

   override suspend fun signalRControl(
      id: Int,
      imageUrl: String
   ) {
      try {
         if (id != 0) {
            //NewTweet follow

            job = CoroutineScope(coContextIO).launch {

               val userId = followedUserIdList?.find { it == id }
               if (userId != null) {
                  followNewTweet[userId] = imageUrl
                  followNewTweetM.postValue(followNewTweet)

               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControl-Ex", e.toString())
      }

   }


   override suspend fun notificationList(): List<Any> {
      val tweetList: MutableList<Any> = mutableListOf()
      tweetList.addAll(localSource.notificationListLike())
      tweetList.addAll(localSource.notificationListTweet())
      return tweetList
   }

   override fun hashMapNewTweet(): MutableLiveData<HashMap<Int, String>> = followNewTweetM

}

