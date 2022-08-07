package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.bhdr.twitterclone.room.TweetDaoInterface
import com.bhdr.twitterclone.room.TweetsRoomModel
import com.bhdr.twitterclone.room.UsersRoomModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class TweetRepository(private val tweetDao: TweetDaoInterface) {
   enum class MainStatus { LOADING, ERROR, DONE }

   private val db = Firebase.storage

   val mainStatus = MutableLiveData<MainStatus>()

   val app = ContentType.Application
   val tweets = MutableLiveData<List<Posts>>()
   val tweetsRoomList = MutableLiveData<List<TweetsRoomModel?>?>()
   val tweetsRoomListLikeToInsert = MutableLiveData<List<TweetsRoomModel?>?>()
   private var tweetsRoomPost: List<TweetsRoomModel?> = listOf()


   val tweetAdded = MutableLiveData<Boolean>()
   private var isObserve: Boolean = true

   //   private suspend fun getBitmap(): Bitmap {
//      val loading = ImageLoader(Context)
//      val request = ImageRequest.Builder(this)
//         .data("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
//         .build()
//
//      val result = (loading.execute(request) as SuccessResult).drawable
//      return (result as BitmapDrawable).bitmap
//   }
   suspend fun getTweets(id: Int) {
      try {

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

   suspend fun postLiked(id: Int, count: Int, userId: Int) {
      try {


         val response = CallApi.retrofitServiceMain.postLiked(userId, id, count)
         if (response.isSuccessful) {
            isObserve = false
            Log.e("TAG", tweetsRoomPost.toString())
            tweetsRoomPost.find { it!!.id == id }?.apply {
               postLike = response.body()
               isLiked = true

            }

            tweetsInsert(tweetsRoomPost)
            Log.e("TAG", tweetsRoomPost.toString())

            Log.e("TAG", response.body().toString())

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

         if (isObserve) {
            Log.e("class", "true")
            tweetsRoomModelConvertPostModel()
         } else {
            isObserve = true
            Log.e("class", "false")
         }
      } catch (e: ClassCastException) {
         Log.e("classEx", e.toString())
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
      mainStatus.value = MainStatus.LOADING
      val tweetsRoomModelList = tweetDao.allTweet()
      if (tweetsRoomModelList.isNotEmpty()) {
//         tweetsRoomModelList.forEach {
//            var users: Users
//            it!!.user.apply {
//               users = Users(
//                  id = this!!.id.toInt(),
//                  photoUrl = photoUrl,
//                  userName = userName,
//                  name = name,
//                  date = null,
//                  email = null,
//                  followers = null,
//                  messages = null,
//                  phone = null,
//                  userPassword = null,
//                  posts = null
//               )
//            }
//            it.apply {
//               tweetsRoomPost.add(
//                  Posts(
//                     date = date,
//                     id = id,
//                     postContent = postContent,
//                     postImageUrl = postImageUrl,
//                     postLike = postLike,
//                     user = users,
//                     userId = userId,
//                     followers = null,
//                     tags = null
//                  )
//               )
//            }
         tweetsRoomPost = tweetsRoomModelList
         tweetsRoomList.value = tweetsRoomModelList.reversed()
         tweetsRoomListLikeToInsert.value = tweetsRoomModelList.reversed()
         // }
      } else {
         tweetsRoomList.value = null

      }
   }

}

