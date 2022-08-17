package com.bhdr.twitterclone.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL_LOGIN = "http://192.168.3.151:9009/api/LogInUp/"
const val BASE_URL_MAIN = "http://192.168.3.151:9009/api/Main/"

val moshi = Moshi.Builder()
   .add(KotlinJsonAdapterFactory())
   .build()

val retrofitLogInUp: Retrofit = Retrofit.Builder()
   .addConverterFactory(
      MoshiConverterFactory
         .create(moshi)
   ).baseUrl(BASE_URL_LOGIN).build()

val retrofitMain: Retrofit = Retrofit.Builder()
   .addConverterFactory(
      MoshiConverterFactory
         .create(moshi)
   ).baseUrl(BASE_URL_MAIN).build()



