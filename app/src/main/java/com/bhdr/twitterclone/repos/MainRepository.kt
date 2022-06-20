package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    private val _mainStatus = MutableLiveData<MainStatus>()
    val mainStatus: LiveData<MainStatus>
        get() = _mainStatus

    private val _sharedFlowPost = MutableSharedFlow<List<Posts>>()
    val sharedFlow = _sharedFlowPost.asSharedFlow()

    fun getPosts(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _mainStatus.value = MainStatus.LOADING
            val requset = CallApi.retrofitServiceMain.getPosts(id)
            if (requset.isSuccessful) {
                _mainStatus.value = MainStatus.DONE
                _sharedFlowPost.emit(requset.body()!!)
                Log.e("TAG", _sharedFlowPost.toString())
            } else if (!requset.isSuccessful) {
                _mainStatus.value = MainStatus.ERROR
            }
        }

    }
}

