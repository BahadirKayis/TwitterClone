package com.bhdr.twitterclone.repos

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
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetDaoInterface
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mikepenz.materialdrawer.util.ifNotNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File


class TweetRepository(
   private val tweetDao: TweetDaoInterface,
   private val application: Application
) {

   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()


   val tweets = MutableLiveData<List<Posts>?>()
   val tweetsRoomList = MutableLiveData<List<TweetsRoomModel>?>()


   val tweetAdded = MutableLiveData<Boolean>()


   private lateinit var imageUri: String
   private var haveImageUri: Boolean = false

   val mutableFollowNewTweetHashMap = MutableLiveData<HashMap<Int, String>>()

   private var hashMapFollowNewTweetHashMap: HashMap<Int, String> = HashMap()
   suspend fun getTweets(id: Int) {
      try {
         val request = CallApi.retrofitServiceMain.getTweets(id)

         if (request.isSuccessful) {

            mainStatus.value = MainStatus.DONE

            tweets.value = request.body()!!
            isNewTweet(request.body()!!, tweetsRoomList.value)
            Log.e("TweetRepoGetPosts", request.body().toString())
         } else if (!request.isSuccessful) {
            mainStatus.value = MainStatus.ERROR
         }
      } catch (e: Exception) {
         mainStatus.value = MainStatus.ERROR
         Log.e("TweetRepoGetPostsEX", e.toString())
         if (e.message == "timeout") {
            getTweets(id)
         }
      }

   }

   suspend fun tweetLiked(id: Int, count: Int, userId: Int) {
      try {
         val response = CallApi.retrofitServiceMain.postLiked(userId, id, count)

         if (response.isSuccessful) {
            tweetDao.tweetIsLiked(id, response.body()!!, true)
         }

      } catch (e: Exception) {
         Log.e("postLiked", e.toString())
      }
   }

   suspend fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
      mainStatus.value = MainStatus.LOADING
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
                        CallApi.retrofitServiceMain.addTweet(id, tweetText, tweetPictureUrl)
                     if (addTweetResult.isSuccessful) {
                        mainStatus.value = MainStatus.DONE
                        tweetAdded.value = addTweetResult.body()
                     } else if (!addTweetResult.isSuccessful) {
                        mainStatus.value = MainStatus.ERROR
                        tweetAdded.value = false
                     }
                     Log.e("TweetRepositoryAddTweet", addTweetResult.errorBody().toString())
                  }

               }
            }
         } else {
            val addTweetResult =
               CallApi.retrofitServiceMain.addTweet(id, tweetText, "null")
            if (addTweetResult.isSuccessful) {
               mainStatus.value = MainStatus.DONE
               tweetAdded.value = addTweetResult.body()
            } else if (!addTweetResult.isSuccessful) {
               mainStatus.value = MainStatus.ERROR
               tweetAdded.value = false

            }

         }
      } catch (e: Exception) {
         mainStatus.value = MainStatus.ERROR
         tweetAdded.value = false
         Log.e("TweetRepositoryAdTwEx", e.toString())
      }

   }

   //room
   private suspend fun tweetsInsert(tweets: List<TweetsRoomModel?>) {
      try {
         tweetDao.addTweet(tweets)
         delay(500)
         getTweetsRoom()
      } catch (e: ClassCastException) {
         Log.e("tweetsInsertEx", e.toString())
      }
   }

   private suspend fun tweetsUpdate(tweets: List<Posts>) {
      tweets.forEach {
         tweetDao.updateTweet(it.id!!, it.postLike!!)

      }
      getTweetsRoom()
   }

   suspend fun getTweetsRoom() {

      val tweetsRoomModelList = tweetDao.allTweet()

      if (tweetsRoomModelList != null) {

         tweetsRoomList.value = tweetsRoomModelList

      } else {
         tweetsRoomList.value = null

      }
   }

   suspend fun tweetsRoomConvertAndAdd(lTweet: List<Posts?>) {
      mainStatus.value = MainStatus.LOADING
      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         lTweet.forEach {
            var userRoomModel: UsersRoomModel? = null
            imageUri =
               createImageUri(it!!.user?.id.toString() + "_" + it.user?.userName + "_user_profile.png")
            if (!haveImageUri) {
               val bmp = getBitmap(it.user!!.photoUrl!!)//fotoğraf indiriliyor
               bmp.ifNotNull { storeBitmap(bmp) }//fotoğraf kayıt edilyior
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
         mainStatus.value = MainStatus.DONE

      } catch (e: Exception) {
         Log.e("tweetsRoomConvertAndAdd", e.toString())
      }
   }

   private suspend fun getBitmap(url: String): Bitmap {
      try {
         val loading = ImageLoader(application)
         val request = ImageRequest.Builder(application)
            .data(url)
            .build()
         val result = (loading.execute(request) as SuccessResult).drawable
         return (result as BitmapDrawable).bitmap
      } catch (e: Exception) {
         throw  e
      }

   }

   private fun createImageUri(photoName: String): String {
      val image = File(application.filesDir, photoName)

      haveImageUri = image.isFile

      return FileProvider.getUriForFile(
         application,
         "com.bhdr.twitterclone.fileProvider",
         image
      ).toString()

   }

   private fun storeBitmap(bmp: Bitmap) {
      val outputStream =
         application.contentResolver.openOutputStream(Uri.parse(imageUri))
      bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      outputStream!!.close()
   }


   private suspend fun isNewTweet(cloudTweet: List<Posts>, roomTweet: List<TweetsRoomModel>?) {

      val tweetRoomUpdateList: MutableList<Posts> = mutableListOf()
      val tweetCloudAddToRoomList: MutableList<Posts> = mutableListOf()
      try {
         if (roomTweet == null) {
            newTweetButton(cloudTweet, tweetRoomUpdateList)
         } else {
            if (cloudTweet.isNotEmpty()) {
               if (roomTweet.size != cloudTweet.size) {
                  roomTweet.forEach { itCloud ->
                     val tweet: Posts? = cloudTweet.find { itRoom ->
                        itCloud.id != itRoom.id
                     }
                     if (tweet != null) {
                        hashMapFollowNewTweetHashMap[tweet.id!!] = tweet.user?.photoUrl.toString()
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
         Log.e("isNewTweet", e.toString())
      }
   }

   private suspend fun newTweetButton(addTweet: List<Posts>?, updateTweet: List<Posts>?) {
      if (updateTweet != null) {
         tweetsUpdate(updateTweet)
      }
      if (addTweet != null) {
         mutableFollowNewTweetHashMap.value = hashMapFollowNewTweetHashMap
         tweets.value = addTweet
      }
   }
}

