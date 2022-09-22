package com.bhdr.twitterclone.data.repos

import com.bhdr.twitterclone.data.model.remote.Users
import com.bhdr.twitterclone.domain.repository.SearchRepository
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain

class SearchRepositoryImpl(private val remoteSource: RemoteDataSourceMain) :
   SearchRepository {


   override suspend fun getSearchFollowUser(id: Int): List<Users> =
      try {
         remoteSource.getSearchNotFollow(id).body()!!
      } catch (e: Exception) {
         throw Exception("")
      }

   override suspend fun getTags(): List<String> = try {
      remoteSource.getPopularTags().body()!!
   } catch (e: Exception) {
      throw Exception("")
   }


   override suspend fun postUserFollow(userId: Int, followId: Int): Boolean =
      remoteSource.postUserFollow(userId, followId).body()!!


   override suspend fun followUserList(userId: Int): List<Int>? =
      remoteSource.getFollowedUserIdList(userId).body()

   override suspend fun searchUser(userName: String): List<Users>? =
      remoteSource.getSearchUser(userName)?.body()

}