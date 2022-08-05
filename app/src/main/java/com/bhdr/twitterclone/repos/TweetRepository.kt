package com.bhdr.twitterclone.repos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.helperclasses.userId
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetDaoInterface
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking


class TweetRepository(private val tweetDao: TweetDaoInterface) {
   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()


   val tweets = MutableLiveData<List<Posts>>()
   val tweetsRoomList = MutableLiveData<List<Posts?>?>()
   private val tweetsRoomPost: MutableList<Posts> = mutableListOf()

   val liked = MutableLiveData<Int>()

   val tweetAdded = MutableLiveData<Boolean>()

   suspend fun getTweets(id: Int) {
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
            getTweets(id)
         }

      }

   }

   suspend fun postLiked(id: Int, count: Int, context: Context) {
      val request = CallApi.retrofitServiceMain.postLiked(context.userId(), id, count)
      if (request.isSuccessful) {
         liked.value = request.body()!!
         getTweets(context.userId())
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

   suspend fun tweetsInsert(tweets: List<TweetsRoomModel>) {
      try {
         tweetDao.addTweet(tweets)
      } catch (e: ClassCastException) {
         Log.e("class", e.toString())
      }
   }

   suspend fun tweetsRoomConvertAndAdd(it: List<Posts>) {
      val tweetsAddRoom: MutableList<TweetsRoomModel> = mutableListOf()
      try {
         it.forEach {
            var userRoomModel: UsersRoomModel? = null
            it.user?.apply {
               userRoomModel = UsersRoomModel(id, photoUrl, userName, name)
            }
            it.apply {
               tweetsAddRoom.add(
                  TweetsRoomModel(
                     date,
                     id,
                     postContent,
                     postImageUrl,
                     postLike,
                     userRoomModel,
                     userId
                  )
               )
            }
         }
         tweetsInsert(tweetsAddRoom)
      } catch (e: Exception) {
         Log.e("ex", e.toString())
      }
   }

   suspend fun tweetsRoomModelConvertPostModel() {
      val tweetsRoomModelList = tweetDao.allTweet()
      if (tweetsRoomModelList.isNotEmpty()) {
         tweetsRoomModelList.forEach {
            var users: Users
            it!!.user.apply {
               users = Users(
                  id = this!!.id.toInt(),
                  photoUrl = photoUrl,
                  userName = userName,
                  name = name,
                  date = null,
                  email = null,
                  followers = null,
                  messages = null,
                  phone = null,
                  userPassword = null,
                  posts = null
               )
            }
            it.apply {
               tweetsRoomPost.add(
                  Posts(
                     date = date,
                     id = id,
                     postContent = postContent,
                     postImageUrl = postImageUrl,
                     postLike = postLike,
                     user = users,
                     userId = userId,
                     followers = null,
                     tags = null
                  )
               )
            }
            tweetsRoomList.value = tweetsRoomPost.reversed()
         }
      } else {
         tweetsRoomList.value=null

      }
   }
}

private fun Any?.toInt(): Int {
   val d = 5.25
   return d.toInt()
}

