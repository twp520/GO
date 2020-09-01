package com.colin.go.api

import com.colin.go.util.AppExecutors
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * create by colin
 * 2020/8/25
 */
object ApiClient {

    private const val baseUrl = "https://restapi.amap.com/v3/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val appExecutors = AppExecutors()
}