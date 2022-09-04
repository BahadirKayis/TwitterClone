package com.bhdr.twitterclone.di

import com.bhdr.twitterclone.common.Constants.BASE_URL_LOGIN
import com.bhdr.twitterclone.common.Constants.BASE_URL_MAIN
import com.bhdr.twitterclone.data.source.remote.login.TweetRemoteServiceLogin
import com.bhdr.twitterclone.data.source.remote.main.TweetRemoteServiceMain
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
   fun provideLogInService(): TweetRemoteServiceLogin = Retrofit.Builder()
      .baseUrl(BASE_URL_LOGIN)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(TweetRemoteServiceLogin::class.java)

   @Provides
   @Singleton
   fun provideMainService(): TweetRemoteServiceMain = Retrofit.Builder()
      .baseUrl(BASE_URL_MAIN)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(TweetRemoteServiceMain::class.java)
}