package com.bhdr.twitterclone.di

import com.bhdr.twitterclone.data.source.locale.LocalDatabaseImpl
import com.bhdr.twitterclone.data.source.locale.TweetDaoInterface
import com.bhdr.twitterclone.data.source.remote.login.RemoteDataSourceImplLogin
import com.bhdr.twitterclone.data.source.remote.login.TweetRemoteServiceLogin
import com.bhdr.twitterclone.data.source.remote.main.RemoteDataSourceImplMain
import com.bhdr.twitterclone.data.source.remote.main.TweetRemoteServiceMain
import com.bhdr.twitterclone.domain.source.locale.LocalDataSource
import com.bhdr.twitterclone.domain.source.remote.login.RemoteDataSourceLogin
import com.bhdr.twitterclone.domain.source.remote.main.RemoteDataSourceMain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

   @Provides
   @Singleton
   fun providerRemoteDataSourceLogin(tweetInterFace: TweetRemoteServiceLogin):
           RemoteDataSourceLogin =
      RemoteDataSourceImplLogin(tweetInterFace)

   @Provides
   @Singleton
   fun providerRemoteDataSourceMain(tweetInterFace: TweetRemoteServiceMain):
           RemoteDataSourceMain =
      RemoteDataSourceImplMain(tweetInterFace)

   @Provides
   @Singleton
   fun providerLocalDataSource(localSource: TweetDaoInterface):
           LocalDataSource =
      LocalDatabaseImpl(localSource)

}