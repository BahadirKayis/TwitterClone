package com.bhdr.twitterclone.repos

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.SignalRModel
import com.bhdr.twitterclone.network.CallApi
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainRepository {
   val followCount: MutableLiveData<Int> = MutableLiveData()
   val followedCount: MutableLiveData<Int> = MutableLiveData()
   private var listUserIdImageUrl = HashMap<Int, String>()
   var mutableNotFollowTweetOrLike = MutableLiveData<List<SignalRModel>>()
   var mutableFollowNewTweet = MutableLiveData<HashMap<Int, String>>()
   private var followedUserIdList: List<Int>? = null
   private var mutableNotFollowTweetOrLikeList: MutableList<SignalRModel>? = null

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


   fun tweetSignalR(context: Context) {

      try {
         val hubConnection =
            HubConnectionBuilder.create("http://192.168.3.136:9009/newTweetHub").build()

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
            context.userId().toString(), { imageUrl, userName, name, post ->
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

   suspend fun signalRControl(
      id: Int,
      imageUrl: String,
      userName: String,
      name: String,
      postId: Int,
      post: Posts?
   ) {
      try {

         if (id == 0) {
            //TODO SignalrModel id'si 0 ise tweeti beğenilmiş demek farklı
            //Like tweeti beğinilmiş modele atılacak

            mutableNotFollowTweetOrLikeList?.add(
               SignalRModel(
                  id,
                  imageUrl,
                  userName,
                  name,
                  post!!
               )
            )
            mutableNotFollowTweetOrLike.postValue(mutableNotFollowTweetOrLikeList)
         } else {
            Log.e("TAG", "haveID")
            //NewTweet follow & not follow
            val haveId = followedUserIdList?.find { it == id }
            if (haveId != null) {

               caScope.launch {

                  CoroutineScope(Dispatchers.Main).launch {
                     listUserIdImageUrl[id] = imageUrl
                     Log.e("list", listUserIdImageUrl.values.toTypedArray()[0])

                     mutableFollowNewTweet.value = listUserIdImageUrl

                     Log.e("mutable", mutableFollowNewTweet.value.toString())
                  }
               }
            } else {
               //Takipe etmiyor bildirim ekranında gösterilecek modelde olacak
               val getPost: Posts = tweetNew(postId)
               mutableNotFollowTweetOrLikeList?.add(
                  SignalRModel(
                     id,
                     imageUrl,
                     userName,
                     name,
                     getPost
                  )
               )
               mutableNotFollowTweetOrLike.postValue(mutableNotFollowTweetOrLikeList)
            }
         }
      } catch (e: Exception) {
         Log.e("signalRControlEx", e.toString())
      }

   }
}