package com.bhdr.twitterclone.repos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetDaoInterface
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking


class TweetRepository {
   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()


   val tweets = MutableLiveData<List<Posts>>()


   val liked = MutableLiveData<Int>()

   val tweetAdded = MutableLiveData<Boolean>()



//val allTweet=MutableLiveData<List<Posts>>()

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

   //suspend fun tweetsInsert(tweets:List<TweetsRoomModel>)=localTweets.addTweet( tweets)


}

