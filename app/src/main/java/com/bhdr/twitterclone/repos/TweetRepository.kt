package com.bhdr.twitterclone.repos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking


class TweetRepository {
   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()


   val tweets = MutableLiveData<List<Posts>>()


   val liked = MutableLiveData<Int>()

   val tweetAdded = MutableLiveData<Boolean>()
   private var listUserIdImageUrl = HashMap<Int, String>()

//val allTweet=MutableLiveData<List<Posts>>()

   suspend fun getPosts(id: Int) {
      try {
         mainStatus.value = MainStatus.LOADING
         val request = CallApi.retrofitServiceMain.getTweets(id)
         if (request.isSuccessful) {
            mainStatus.value = MainStatus.DONE
            tweets.value = request.body()!!
            Log.e("TweetRepoGetPosts", request.body().toString())
         } else if (!request.isSuccessful) {
            mainStatus.value = MainStatus.ERROR
         }
      } catch (e: Exception) {
         mainStatus.value = MainStatus.ERROR
         Log.e("TweetRepoGetPostsEX", e.toString())
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

   //suspend fun tweetsInsert(tweets:List<TweetsRoomModel>)=localTweets.addTweet( tweets)
   suspend fun isNewTweet(roomTweet: List<TweetsRoomModel>, cloudTweet: List<Posts>) {
      if (roomTweet.isNotEmpty() && cloudTweet.isNotEmpty()) {
         if (roomTweet.size != cloudTweet.size) {
            val Notnus=cloudTweet.intersect(roomTweet.toSet())
            val common = findCommon(roomTweet, cloudTweet)
            Log.e("Notnus", Notnus.toString() )
            Log.e("common", Notnus.toString() )
//            cloudTweet.forEach { itCloud ->
//               roomTweet.find { itRoom ->
//                  itCloud.id==itRoom.id -> RoomModel
//
//
//               }
//            }
         }
      }
   }

 private  fun <T> findCommon(first: List<T>, second: List<T>): Set<T> {
      val common = first.toMutableSet();
      common.retainAll(second.toSet())
      return common
   }

}

