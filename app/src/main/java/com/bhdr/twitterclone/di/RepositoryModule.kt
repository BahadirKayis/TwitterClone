package com.bhdr.twitterclone.di

import android.app.Application
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import com.bhdr.twitterclone.data.repos.MainRepositoryImpl
import com.bhdr.twitterclone.data.repos.SearchRepositoryImpl
import com.bhdr.twitterclone.data.repos.TweetRepositoryImpl
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

   @Provides
   @Singleton
   fun provideTweetRepository(
      tweetDao: LocalDataSource,
      app: Application,
      remoteSource: RemoteDataSourceMain,

      ): TweetRepository = TweetRepositoryImpl(tweetDao, app, remoteSource)


   @Provides
   @Singleton
   fun provideMainRepository(
      tweetDao: LocalDataSource,
      remoteSourceMain: RemoteDataSourceMain,
      remoteSourceLogin: RemoteDataSourceLogin
   ): MainRepositoryImpl {
      return MainRepositoryImpl(tweetDao, remoteSourceMain, remoteSourceLogin)
   }

   @Provides
   @Singleton
   fun provideLoginUpRepository(remoteSource: RemoteDataSourceLogin): LoginUpRepositoryImpl {
      return LoginUpRepositoryImpl(remoteSource)
   }

   @Provides
   @Singleton
   fun provideSearchRepository(remoteSource: RemoteDataSourceMain): SearchRepositoryImpl {
      return SearchRepositoryImpl(remoteSource)
   }


}