package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    private val _mainStatus = MutableLiveData<MainStatus>()
    val mainStatus: LiveData<MainStatus>
        get() = _mainStatus

    private val _sharedFlowPost = MutableSharedFlow<List<Posts>>()
    val sharedFlow = _sharedFlowPost.asSharedFlow()

    suspend fun getPosts(id: Int) {
        try {
            _mainStatus.value = MainStatus.LOADING
            val request = CallApi.retrofitServiceMain.getPosts(id)
            if (request.isSuccessful) {
                _mainStatus.value = MainStatus.DONE
                _sharedFlowPost.emit(request.body()!!)
                Log.e("TAG", request.body().toString())
            } else if (!request.isSuccessful) {
                _mainStatus.value = MainStatus.ERROR
            }
        } catch (e: Exception) {
            Log.e("ex", e.toString())
        }

    }
}

