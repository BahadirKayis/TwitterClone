package com.bhdr.twitterclone.repos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.*


class TweetRepository {
   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()


   val tweets = MutableLiveData<List<Posts>>()


   val liked = MutableLiveData<Int>()

   val tweetAdded = MutableLiveData<Boolean>()

   private var followedUserIdList: List<Int>? = null

   suspend fun getPosts(id: Int) {
      try {
         mainStatus.value = MainStatus.LOADING
         val request = CallApi.retrofitServiceMain.getTweets(id)
         if (request.isSuccessful) {
            mainStatus.value = MainStatus.DONE
            tweets.value = request.body()!!
            Log.e("TAG", request.body().toString())
         } else if (!request.isSuccessful) {
            mainStatus.value = MainStatus.ERROR
         }
      } catch (e: Exception) {
         mainStatus.value = MainStatus.ERROR
         Log.e("ex", e.toString())
         if (e.message == "timeout") {
            getPosts(id)
         }

      }

   }

   suspend fun postLiked(id: Int, count: Int, context: Context) {
      val request = CallApi.retrofitServiceMain.postLiked(context.userId(), id, count)
      if (request.isSuccessful) {
         liked.value = request.body()!!
         getPosts(context.userId())
      } else {

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
                     Log.e("photo", addTweetResult.errorBody().toString())
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
               Log.e("TAG", addTweetResult.toString())
            }
            Log.e("TAG", addTweetResult.toString())
            Log.e("TAG", addTweetResult.message().toString())
            Log.e("TAG", addTweetResult.headers().toString())
         }
      } catch (e: Exception) {
         mainStatus.value = MainStatus.ERROR
         tweetAdded.value = false
         Log.e("TAG", e.toString())
      }

   }

   suspend fun getFollowedUserIdList(userId: Int) {
      val response = CallApi.retrofitServiceMain.getFollowedUserIdList(userId)
      if (response.isSuccessful) {
         followedUserIdList = response.body()
      }
      Log.e("response", response.body().toString())
   }


   fun tweetSignalR() {
      try {


         val hubConnection =
            HubConnectionBuilder.create("http://192.168.3.136:9009/newTweetHub").build()

         if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
            hubConnection.start()

         }
         hubConnection.on("newTweet", { id, imageUrl ->
            Log.e("id", id.toString())
            Log.e("imageUrl", imageUrl.toString())

            followedControl(id.toInt(), imageUrl)

         }, String::class.java, String::class.java)
      } catch (e: Exception) {
         Log.e("tweetSignalRException", e.toString())
      }
   }

   private var listUserIdImageUrl = HashMap<Int, String>()
   var mutableListUserIdImageUrl = MutableLiveData<HashMap<Int, String>>()
   private fun followedControl(id: Int, imageUrl: String) {

      val haveId = followedUserIdList?.find { it == id }
      haveId.let {
         try {
            listUserIdImageUrl.put(id, imageUrl)
            //  CoroutineScope(Dispatchers.IO).launch {
            mutableListUserIdImageUrl.postValue(listUserIdImageUrl)

            // }

         } catch (e: Exception) {
            Log.e("listUserIdImageUrlEx", e.toString())
         }


      }
   }
}

