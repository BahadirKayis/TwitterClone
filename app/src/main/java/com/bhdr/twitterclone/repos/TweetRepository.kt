package com.bhdr.twitterclone.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.bhdr.twitterclone.models.Posts
import com.bhdr.twitterclone.network.CallApi
import io.grpc.InternalChannelz.id
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TweetRepository {
    enum class MainStatus { LOADING, ERROR, DONE }

    private val _mainStatus = MutableLiveData<MainStatus>()
    val mainStatus: LiveData<MainStatus>
        get() = _mainStatus

    private val _sharedFlowPost = MutableLiveData<List<Posts>>()
    val sharedFlow:LiveData<List<Posts>>
    get() = _sharedFlowPost

    private  val _liked=MutableLiveData<Int>()
    val liked:LiveData<Int>
    get() = _liked

    suspend fun getPosts(id: Int) {
        try {
            _mainStatus.value = MainStatus.LOADING
            val request = CallApi.retrofitServiceMain.getPosts(id)
            if (request.isSuccessful) {
                _mainStatus.value = MainStatus.DONE
                _sharedFlowPost.value=request.body()!!
                Log.e("TAG", request.body().toString())
            } else if (!request.isSuccessful) {
                _mainStatus.value = MainStatus.ERROR
            }
        } catch (e: Exception) {
            Log.e("ex", e.toString())
            if(e.message=="timeout")
            {
                getPosts(id)
            }
            _mainStatus.value = MainStatus.ERROR
        }

    }

    suspend fun postLiked(id:Int, count:Int){
       val request=CallApi.retrofitServiceMain.postLiked(id,count)
        if(request.isSuccessful){
            _liked.value = request.body()!!
            getPosts(1)
        }

        else{

        }
    }
}

