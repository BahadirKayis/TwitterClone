package com.bhdr.twitterclone.di

import android.app.Application
import com.bhdr.twitterclone.data.repos.ActivityRepositoryImpl
import com.bhdr.twitterclone.data.repos.LoginUpRepositoryImpl
import com.bhdr.twitterclone.data.repos.MainTweetRepositoryImpl
import com.bhdr.twitterclone.data.repos.SearchRepositoryImpl
import com.bhdr.twitterclone.domain.repository.ActivityRepository
import com.bhdr.twitterclone.domain.repository.LoginUpRepository
import com.bhdr.twitterclone.domain.repository.SearchRepository
import com.bhdr.twitterclone.domain.repository.TweetRepository
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
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
      @Named("Main") coContextIO: CoroutineDispatcher,
      firebaseStorage: FirebaseStorage

   ): TweetRepository =
      MainTweetRepositoryImpl(tweetDao, app, remoteSource, coContextIO, firebaseStorage)


   @Provides
   @Singleton
   fun provideMainRepository(
      tweetDao: LocalDataSource,
      remoteSourceMain: RemoteDataSourceMain,
      @Named("IO") coContextIO: CoroutineDispatcher,
      application: Application
   ): ActivityRepository =
      ActivityRepositoryImpl(tweetDao, remoteSourceMain, coContextIO, application)

   @Provides
   @Singleton
   fun provideLoginUpRepository(
      remoteSource: RemoteDataSourceLogin,
      firebaseStorage: FirebaseStorage,
      @Named("IO") coContextIO: CoroutineDispatcher
   ):
           LoginUpRepository = LoginUpRepositoryImpl(remoteSource, firebaseStorage, coContextIO)


   @Provides
   @Singleton
   fun provideSearchRepository(remoteSource: RemoteDataSourceMain):
           SearchRepository = SearchRepositoryImpl(remoteSource)


}