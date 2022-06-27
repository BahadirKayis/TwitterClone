package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Tags
import com.bhdr.twitterclone.models.Users
import com.bhdr.twitterclone.network.BASE_URL_MAIN
import com.bhdr.twitterclone.network.CallApi
import com.google.firebase.firestore.auth.User
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class SearchRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    val searchStatus = MutableLiveData<MainStatus>()

    val followUser = MutableLiveData<List<Users>>()
    val tags = MutableLiveData<List<Tags>>()


    suspend fun getSearchFollowUser(id: Int) {

try {


        val response = CallApi.retrofitServiceMain.getSearchNotFollow(id)
        searchStatus.value = MainStatus.LOADING
        if (response.isSuccessful) {
            searchStatus.value = MainStatus.DONE
            followUser.value = response.body()
            Log.e("TAG", response.errorBody().toString() )
            Log.e("TAG", response.toString() )
            Log.e("TAG", response.code().toString())
            Log.e("TAG", response.headers().toString())
        } else {
            Log.e("TAG", response.errorBody().toString() )
            Log.e("TAG", response.toString() )
            Log.e("TAG", response.code().toString())
            Log.e("TAG", response.headers().toString())
            searchStatus.value = MainStatus.ERROR
            //followUser.value = null
       }
}
catch (e: Exception) {
    Log.e("TAG", e.toString() )
}
    }

    suspend fun getTags() {
        var response = CallApi.retrofitServiceMain.getPopularTags()

        if (response.isSuccessful) {
            tags.value = response.body()

        } else {
            Log.e("TAG", "getTags else ", )
            //tags.value = null
        }
    }

}