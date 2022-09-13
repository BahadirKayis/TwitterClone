package com.bhdr.twitterclone.data.repos

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
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
import com.mikepenz.materialdrawer.util.ifNotNull
import kotlinx.coroutines.*
import okio.IOException
import java.io.File
import javax.inject.Named


class TweetRepositoryImpl(
   private val localSource: LocalDataSource,
   private val application: Application,
   private val remoteSource: RemoteDataSourceMain,
   @Named("IO") private val coContextIO: CoroutineDispatcher,
   private val FirebaseStorage: FirebaseStorage
) : TweetRepository {


   private lateinit var imageUri: String

   private var haveImageUri: Boolean = false

   private var hashMapFollowNewTweetHashMap: HashMap<Int, String> = HashMap()

   private var job: Job? = null

   private var followedUserIdList: List<Int>? = null

   override suspend fun getTweets(id: Int): List<Posts>? {
      try {

         val request = remoteSource.getTweets(id)
         if (request.isSuccessful) {
            return request.body()!!
         }

      } catch (e: IOException) {
         Log.e("getTweets-Ex", e.toString())
         if (e.localizedMessage == "timeout") {
            getTweets(id)
         }
      }
      return null
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
   ): Boolean {
      var isTweetAdded = false

      try {
         val reference = FirebaseStorage.reference
         if (tweetImage != null) {

            val imagesReference = reference.child("tweetpictures").child(tweetImageName)
            imagesReference.putFile(tweetImage).addOnSuccessListener {

               val uploadedPictureReference =
                  FirebaseStorage.reference.child("tweetpictures").child(tweetImageName)
               uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                  val tweetPictureUrl = uri.toString()

                  runBlocking {
                     val addTweetResult =
                        remoteSource.addTweet(
                           id, tweetText, tweetPictureUrl,
                           toLongDate().toString()
                        )
                     if (addTweetResult.isSuccessful) {

                        isTweetAdded = addTweetResult.body()!!
                     } else if (!addTweetResult.isSuccessful) {
                        isTweetAdded = false
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

               isTweetAdded = addTweetResult.body()!!
            } else if (!addTweetResult.isSuccessful) {
               isTweetAdded = false
            }

         }
      } catch (e: Exception) {
         isTweetAdded = false
         Log.e("RepositoryAdT-EX", e.toString())
      }
      return isTweetAdded
   }

   //room
   override suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>): List<TweetsRoomModel>? {
      localSource.addTweet(tweets)
      delay(500)
      return getTweetsRoom()
   }

   override suspend fun tweetsUpdate(tweets: List<Posts>): List<TweetsRoomModel>? {
      tweets.forEach {
         localSource.updateTweet(it.id!!, it.postLike!!)
      }
      return getTweetsRoom()
   }

   override suspend fun getTweetsRoom(): List<TweetsRoomModel>? {

      val tweetsRoomModelList = localSource.allTweet()
      Log.i("tweetsRoomModelList", tweetsRoomModelList.toString())
      if (tweetsRoomModelList != null) {
         return tweetsRoomModelList
      }
      return null
   }

   override suspend fun tweetsRoomConvertAndAdd(lTweet: List<Posts?>): List<TweetsRoomModel> {

      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         lTweet.forEach {
            var userRoomModel: UsersRoomModel? = null
            imageUri =
               createImageUri(it!!.userId.toString() + "_" + it.user?.userName + "_user_profile.png")
            if (!haveImageUri) {
               val bmp = getBitmap(it.user!!.photoUrl!!)//Image download
               bmp.ifNotNull { storeBitmap(bmp) }//Image save
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

                  if (it.postImageUrl!!.contains(".webm")) {
                     imageUri = it.postImageUrl.toString()
                  } else {
                     if (!haveImageUri) {
                        val bmp = getBitmap(it.postImageUrl.toString())//Image download
                        bmp.ifNotNull { storeBitmap(bmp) }//Image save
                     }
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
         return tweetsAddRoom

      } catch (e: Exception) {
         Log.e("RoomConvertAndAdd-Ex", e.toString())
      }
return tweetsAddRoom

   }

   override suspend fun getBitmap(url: String): Bitmap {
      Log.e("TAG", url.toString() )
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
   ): Pair<List<Posts>?, List<Posts>?> {

      val tweetRoomUpdateList: MutableList<Posts> = mutableListOf()
      val tweetCloudAddToRoomList: MutableList<Posts> = mutableListOf()

      try {
         if (roomTweet?.size == 0) {
            return (cloudTweet to cloudTweet)
            // newTweetButton(cloudTweet, tweetRoomUpdateList)
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
                  return (tweetCloudAddToRoomList to tweetRoomUpdateList)
                  // newTweetButton(tweetCloudAddToRoomList, tweetRoomUpdateList)
               } else {
                  return (null to tweetRoomUpdateList)
                  //  newTweetButton(null, cloudTweet)
               }
            }
         }
      } catch (e: Exception) {
         Log.e("isNewTweet-Ex", e.toString())
      }
      return (null to null)
   }


   ////////////////
   override suspend fun getFollowedUserIdList(userId: Int) {
      val response = remoteSource.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
      Log.i("getFollowedUserIdList", response.body().toString())
   }


   override fun signalRControl(
      id: Int,
      imageUrl: String
   ): HashMap<Int, String> {
      try {
         if (id != 0) {
            //NewTweet follow
            job = CoroutineScope(coContextIO).launch {
               CoroutineScope(Dispatchers.Main).launch {

                  val userId = followedUserIdList?.find { it == id }

                  if (userId != null) {
                     hashMapFollowNewTweetHashMap[id] = imageUrl
                     //mutableFollowNewTweetSignalR.value = hashMapFollowNewTweetHashMap
                  }
               }
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControl-Ex", e.toString())
      }
      return hashMapFollowNewTweetHashMap
   }


   override suspend fun notificationList(): List<Any> {
      val tweetList: MutableList<Any> = mutableListOf()
      tweetList.addAll(localSource.notificationListLike())
      tweetList.addAll(localSource.notificationListTweet())
      return tweetList
   }


}

