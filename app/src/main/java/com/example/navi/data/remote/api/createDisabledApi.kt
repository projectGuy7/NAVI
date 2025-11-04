package com.example.navi.data.remote.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun createDisabledApi(interceptor: Interceptor): DisabledAPI {
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(DisabledAPI::class.java)
}