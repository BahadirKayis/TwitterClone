package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.CallApi

class SearchRepository {
   enum class MainStatus { LOADING, ERROR, DONE }

   val searchStatus = MutableLiveData<MainStatus>()

   val followUser = MutableLiveData<List<Users>>()
   val followedUser = MutableLiveData<Boolean>()
   val tags = MutableLiveData<List<String>>()

   var followedCount = MutableLiveData<List<Int>>()
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
      val response = CallApi.retrofitServiceMain.getPopularTags()

      if (response.isSuccessful) {
         tags.value = response.body()

      } else {
         Log.e("TAG", "getTags else ")
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

      } catch (e: Exception) {
         searchStatus.value = MainStatus.ERROR
         followedUser.value = false
         Log.e("TAG5", e.toString())
      }
   }

   suspend fun followUserList(userId: Int) {
      val response = CallApi.retrofitServiceMain.getFollowedUserIdList(userId)

      if (response.isSuccessful) {
         followedCount.postValue(response.body())
      }

   }
}