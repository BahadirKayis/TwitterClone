package com.bhdr.twitterclone.di

import com.bhdr.twitterclone.data.source.locale.LocalDatabaseImpl
import com.bhdr.twitterclone.data.source.locale.TweetDaoInterface
import com.bhdr.twitterclone.data.source.remote.login.RemoteDatabaseImplLOGIN
import com.bhdr.twitterclone.data.source.remote.login.TweetRemoteServiceLOGIN
import com.bhdr.twitterclone.data.source.remote.main.RemoteDataSourceImplMain
import com.bhdr.twitterclone.data.source.remote.main.TweetRemoteServiceMAIN
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
   fun providerRemoteDataSourceLogin(tweetInterFace: TweetRemoteServiceLOGIN):
           RemoteDataSourceLogin =
      RemoteDatabaseImplLOGIN(tweetInterFace)

   @Provides
   @Singleton
   fun providerRemoteDataSourceMain(tweetInterFace: TweetRemoteServiceMAIN):
           RemoteDataSourceMain =
      RemoteDataSourceImplMain(tweetInterFace)

   @Provides
   @Singleton
   fun providerLocalDataSource(localSource: TweetDaoInterface):
           LocalDataSource =
      LocalDatabaseImpl(localSource)

}