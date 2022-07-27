package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.network.CallApi

class MainRepository {
   val followCount: MutableLiveData<Int> = MutableLiveData()
   val followedCount: MutableLiveData<Int> = MutableLiveData()

   suspend fun followCount(userId: Int) {
      try {
         val response = CallApi.retrofitServiceMain.getFollowCount(userId)

         if (response.isSuccessful) {
            followCount.postValue(response.body())
         }
         Log.e("TAG", response.errorBody().toString())
      }
      catch (e:Exception) {
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
}