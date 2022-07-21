package com.bhdr.twitterclone.repos

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking

class TweetRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    private val db = Firebase.storage

    val mainStatus = MutableLiveData<MainStatus>()


    val sharedFlowPost = MutableLiveData<List<Posts>>()


    val liked = MutableLiveData<Int>()

    val tweetAdded = MutableLiveData<Boolean>()


    suspend fun getPosts(id: Int) {
        try {
            mainStatus.value = MainStatus.LOADING
            val request = CallApi.retrofitServiceMain.getPosts(id)
            if (request.isSuccessful) {
                mainStatus.value = MainStatus.DONE
                sharedFlowPost.value = request.body()!!
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

    suspend fun postLiked(id: Int, count: Int) {
        val request = CallApi.retrofitServiceMain.postLiked(id, count)
        if (request.isSuccessful) {
            liked.value = request.body()!!
            getPosts(1)
        } else {

        }
    }

    suspend fun addTweet(id: Int, tweetText: String, tweetImageName: String, tweetImage: Uri?) {
        mainStatus.value = MainStatus.LOADING
        try {
        val reference = db.reference
        if  (tweetImage!= null) {

        val imagesReference = reference.child("tweetpictures").child(tweetImageName)
        imagesReference.putFile(tweetImage).addOnSuccessListener {

            val uploadedPictureReference = db.reference.child("tweetpictures").child(tweetImageName)
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
                    Log.e("photo",addTweetResult.errorBody().toString())
                }

            }
        }
        }else {
            val addTweetResult =
                CallApi.retrofitServiceMain.addTweet(id, tweetText, "null")
            if (addTweetResult.isSuccessful) {
                mainStatus.value = MainStatus.DONE
                tweetAdded.value = addTweetResult.body()
            } else if (!addTweetResult.isSuccessful) {
                mainStatus.value = MainStatus.ERROR
                tweetAdded.value = false
                Log.e("TAG",addTweetResult.toString())
            }

        }
        }
        catch (e: Exception) {
            mainStatus.value = MainStatus.ERROR
            tweetAdded.value = false
            Log.e("TAG", e.toString() )
        }

    }
}

