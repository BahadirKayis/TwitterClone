package com.bhdr.twitterclone.domain.repository

import com.bhdr.twitterclone.data.model.remote.Users

interface SearchRepository {
   suspend fun getSearchFollowUser(id: Int): List<Users>
   suspend fun getTags(): List<String>
   suspend fun postUserFollow(userId: Int, followId: Int): Boolean
   suspend fun followUserList(userId: Int): List<Int>?
   suspend fun searchUser(userName: String): List<Users>?


}