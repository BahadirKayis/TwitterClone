package com.bhdr.twitterclone.di

import com.bhdr.twitterclone.common.Constants.BASE_URL_LOGIN
import com.bhdr.twitterclone.common.Constants.BASE_URL_MAIN
import com.bhdr.twitterclone.data.source.remote.login.TweetRemoteServiceLOGIN
import com.bhdr.twitterclone.data.source.remote.main.TweetRemoteServiceMAIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
   @Provides
   @Singleton
   fun provideLogInService(): TweetRemoteServiceLOGIN = Retrofit.Builder()
      .baseUrl(BASE_URL_LOGIN)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(TweetRemoteServiceLOGIN::class.java)

   @Provides
   @Singleton
   fun provideMainService(): TweetRemoteServiceMAIN = Retrofit.Builder()
      .baseUrl(BASE_URL_MAIN)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(TweetRemoteServiceMAIN::class.java)
}