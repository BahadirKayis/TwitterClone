package com.bhdr.twitterclone.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.common.Status
import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain

class SearchRepositoryImpl constructor(private val remoteSource: RemoteDataSourceMain) {


   val searchStatus = MutableLiveData<Status>()

   val followUser = MutableLiveData<List<Users>>()
   val followedUser = MutableLiveData<Boolean>()
   val tags = MutableLiveData<List<String>>()

   var followedCount = MutableLiveData<List<Int>>()
   suspend fun getSearchFollowUser(id: Int) {
      try {
         val response = remoteSource.getSearchNotFollow(id)
         searchStatus.value = Status.LOADING
         if (response.isSuccessful) {
            searchStatus.value = Status.DONE
            followUser.value = response.body()

         } else {
            searchStatus.value = Status.ERROR
            //followUser.value = null
         }
      } catch (e: Exception) {
         Log.e("getSearchFollowUser", e.toString())
      }
   }

   suspend fun getTags() {
      val response = remoteSource.getPopularTags()

      if (response.isSuccessful) {
         tags.value = response.body()

      } else {
         Log.e("TAG", "getTags else ")
      }
   }

   suspend fun postUserFollow(userId: Int, followId: Int) {
      try {
         searchStatus.value = Status.LOADING

         val response = remoteSource.postUserFollow(userId, followId)


         if (response.isSuccessful) {
            followedUser.value = response.body()
            searchStatus.value = Status.DONE

         } else {
            searchStatus.value = Status.ERROR
            followedUser.value = false

         }

      } catch (e: Exception) {
         searchStatus.value = Status.ERROR
         followedUser.value = false
         Log.e("TAG5", e.toString())
      }
   }

   suspend fun followUserList(userId: Int) {
      val response = remoteSource.getFollowedUserIdList(userId)

      if (response.isSuccessful) {
         followedCount.postValue(response.body())
      }

   }
}