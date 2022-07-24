package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Tags
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.BASE_URL_MAIN
import com.bhdr.twitterclone.network.CallApi

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class SearchRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    val searchStatus = MutableLiveData<MainStatus>()

    val followUser = MutableLiveData<List<Users>>()
    val followedUser = MutableLiveData<Boolean>()
    val tags = MutableLiveData<List<String>>()


    suspend fun getSearchFollowUser(id: Int) {

        try {
            val response = CallApi.retrofitServiceMain.getSearchNotFollow(id)
            searchStatus.value = MainStatus.LOADING
            if (response.isSuccessful) {
                searchStatus.value = MainStatus.DONE
                followUser.value = response.body()

            } else {
                searchStatus.value = MainStatus.ERROR
                //followUser.value = null
            }
        } catch (e: Exception) {
            Log.e("getSearchFollowUser", e.toString())
        }
    }

    suspend fun getTags() {
        var response = CallApi.retrofitServiceMain.getPopularTags()

        if (response.isSuccessful) {
            tags.value = response.body()

        } else {
            Log.e("TAG", "getTags else ")
            //tags.value = null
        }
    }

    suspend fun postUserFollow(userId: Int, followId: Int) {
        try {
            searchStatus.value = MainStatus.LOADING

            val response = CallApi.retrofitServiceMain.postUserFollow(userId, followId)


            if (response.isSuccessful) {
                followedUser.value = response.body()
                searchStatus.value = MainStatus.DONE

            } else {
                searchStatus.value = MainStatus.ERROR
                followedUser.value = false

            }
            Log.e("TAG1", response.errorBody().toString())
            Log.e("TAG2", response.toString())
            Log.e("TAG3", response.code().toString())
            Log.e("TAG4", response.headers().toString())
        } catch (e: Exception) {
            searchStatus.value = MainStatus.ERROR
            followedUser.value = false
            Log.e("TAG5", e.toString())
        }
    }
}