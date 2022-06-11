package com.bhdr.twitterclone.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

    public const val BASE_URL_LOGIN = "http://192.168.3.136:9009/api/LogInUp/"
    public const val BASE_URL_MAIN = "http://192.168.3.136:9009/api/Main/"
    public val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    public val retrofit= Retrofit.Builder()
        .addConverterFactory(
            MoshiConverterFactory
            .create(moshi)).baseUrl(BASE_URL_LOGIN).build()

