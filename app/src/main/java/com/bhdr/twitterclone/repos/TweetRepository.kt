package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi

class TweetRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    val mainStatus = MutableLiveData<MainStatus>()


    val sharedFlowPost = MutableLiveData<List<Posts>>()


    val liked = MutableLiveData<Int>()


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
}

