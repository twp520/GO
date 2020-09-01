package com.colin.go.api

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * create by colin
 * 2020/8/19
 */
interface ApiService {

    @GET("ip")
    suspend fun locationByIP(
        @Query("output") output: String,
        @Query("key") key: String
    ): IPLocation

    @GET("weather/weatherInfo")
    suspend fun getWeatherBySuspend(@QueryMap map: Map<String, String>): WeatherNet

    @GET("weather/weatherInfo")
    fun getWeatherByNormal(@QueryMap map: Map<String, String>): Call<WeatherNet>


    @GET("ip")
    fun locationByIPNormal(
        @Query("output") output: String,
        @Query("key") key: String
    ): Call<IPLocation>


    @GET("weather/weatherInfo")
    fun getWeatherByLiveData(@QueryMap map: Map<String, String>): LiveData<ApiResponse<WeatherNet>>

    @GET("ip")
    fun locationByIPLiveData(
        @Query("output") output: String,
        @Query("key") key: String
    ): LiveData<ApiResponse<IPLocation>>
}