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

class MainRepository {
   val followCount: MutableLiveData<Int> = MutableLiveData()
   val followedCount: MutableLiveData<Int> = MutableLiveData()
   private var listUserIdImageUrl = HashMap<Int, String>()
   var mutableNotFollowTweetOrLike = MutableLiveData<List<SignalRModel>>()
   var mutableFollowNewTweet = MutableLiveData<HashMap<Int, String>>()
   private var followedUserIdList: List<Int>? = null
   private var mutableNotFollowTweetOrLikeList: MutableList<SignalRModel>? = null

   suspend fun followCount(userId: Int) {
      try {
         val response = CallApi.retrofitServiceMain.getFollowCount(userId)

         if (response.isSuccessful) {
            followCount.postValue(response.body())
         }
         Log.e("TAG", response.errorBody().toString())
      } catch (e: Exception) {
         Log.e("Ex", e.toString())

      }
   }

   suspend fun followedCount(userId: Int) {
      val response = CallApi.retrofitServiceMain.getFollowedCount(userId)

      if (response.isSuccessful) {
         followedCount.postValue(response.body())
      }
      Log.e("TAG", response.errorBody().toString())
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
         hubConnection.on(
            "newTweet",
            { id, imageUrl, userName, name, post ->
               Log.e("id", id.toString())
               Log.e("imageUrl", imageUrl.toString())

               signalRControl(id.toInt(), imageUrl, userName, name, post)
            },
            String::class.java,
            String::class.java,
            String::class.java,
            String::class.java,
            Posts::class.java
         )

//Like imagUrl.PhotoUrl,imagUrl.UserName,imagUrl.Name,post
         hubConnection.on(
            context.userId().toString(), { imageUrl, userName, name, post ->

               signalRControl(0, imageUrl, userName, name, post)

            }, String::class.java,
            String::class.java,
            String::class.java,
            Posts::class.java
         )

      } catch (e: Exception) {
         Log.e("tweetSignalRException", e.toString())
      }
   }


   private fun signalRControl(
      id: Int,
      imageUrl: String,
      userName: String,
      name: String,
      post: Posts
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
                  post
               )
            )


            mutableNotFollowTweetOrLike.postValue(mutableNotFollowTweetOrLikeList)
         } else {
            //NewTweet follow & not follow
            val haveId = followedUserIdList?.find { it == id }
            if (haveId != null) {
               //boş değilse bunu takip ediyor mainscreende üstte resmi açılacak

               listUserIdImageUrl.put(id, imageUrl)

               mutableFollowNewTweet.postValue(listUserIdImageUrl)
            } else {
               //Takipe etmiyor bildirim ekranında gösterilecek modelde olacak
               mutableNotFollowTweetOrLikeList?.add(
                  SignalRModel(
                     id,
                     imageUrl,
                     userName,
                     name,
                     post
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