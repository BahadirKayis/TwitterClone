package com.bhdr.twitterclone.di

import android.content.Context
import androidx.room.Room
import com.bhdr.twitterclone.data.source.locale.TweetDaoInterface
import com.bhdr.twitterclone.data.source.locale.TweetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

   @Provides
   @Singleton
   fun provideFavoritesRoomDB(@ApplicationContext appContext: Context): TweetsDatabase =
      Room.databaseBuilder(
         appContext,
         TweetsDatabase::class.java,
         "tweetsdatabese.db"
      ).build()

   @Provides
   @Singleton
   fun provideCoinsDAO(tweetDao: TweetsDatabase): TweetDaoInterface =
      tweetDao.tweetDao()
}