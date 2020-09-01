package com.colin.go.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.colin.go.api.*
import com.colin.go.bean.GD_KEY
import com.colin.go.bean.STATUS_OK
import com.colin.go.bean.UIResult
import com.colin.go.bean.WeatherInfo
import com.colin.go.db.GoDatabase
import com.colin.go.db.WeatherDao
import com.colin.go.util.Logger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * create by colin
 * 2020/8/25
 */
class WeatherRepository(private val context: Context) {

    private val dao: WeatherDao by lazy {
        GoDatabase.getInstance(context)
            .getWeatherDao()
    }

    private val data = MutableLiveData<UIResult<WeatherInfo?>>()
    private val rateLimiter: RateLimiter<String> =
        RateLimiter(5, TimeUnit.MINUTES)
    private val rateKey = "new_weather"


    //用协程的方式来实现。
    suspend fun getWeatherBySuspend() {
        val queryWeatherList = dao.queryWeather()
        val queryWeather = queryWeatherList.firstOrNull()
        val needUpdate: Boolean
        data.value =
            if (queryWeather == null) {
                needUpdate = true
                UIResult.loading()
            } else {
                needUpdate = rateLimiter.shouldFetch(queryWeather.createTime)
                UIResult.content(queryWeather)
            }
        if (needUpdate) {
            val locationByIP = ApiClient.apiService
                .locationByIP("json", GD_KEY)
            if (STATUS_OK == locationByIP.status) {
                val params = getWeatherParams(locationByIP)
                val weatherNet = ApiClient.apiService.getWeatherBySuspend(params)
                val weatherInfo = weatherNet.lives?.firstOrNull()
                if (weatherInfo != null) {
                    data.value = UIResult.content(weatherInfo)
                    dao.insertSuspend(weatherInfo)
                }
            }
        }
    }


    //通过普通方式来实现。
    fun getWeatherByNormal(): LiveData<UIResult<WeatherInfo>> {
        refreshDBIfNeed()
        return dao.queryWeatherByNormal().map {
            val firstOrNull = it.firstOrNull()
            if (firstOrNull == null)
                UIResult.loading()
            else UIResult.content(firstOrNull)
        }
    }

    private fun refreshDBIfNeed() {
        val shouldFetch = rateLimiter.shouldFetch(rateKey)
        if (shouldFetch) {
            Executors.newSingleThreadExecutor().submit {
                val locationByIP = ApiClient.apiService
                    .locationByIPNormal("json", GD_KEY).execute()
                if (locationByIP.isSuccessful && locationByIP.body() != null) {
                    val weatherNet =
                        ApiClient.apiService.getWeatherByNormal(getWeatherParams(locationByIP.body()!!))
                            .execute()
                    val info = weatherNet.body()?.lives?.firstOrNull()
                    Logger.i("get for net: ${info?.toString()}")
                    if (info != null) {
                        dao.insertNormal(info)
                    }
                }
            }
        }
    }


    //通过boundResource封装来实现
    fun getWeatherByBoundResource(): LiveData<UIResult<WeatherInfo?>> {
        val boundResource =
            object : NetworkBoundResource<WeatherInfo?, WeatherNet>(ApiClient.appExecutors) {

                override fun loadFromDb(): LiveData<WeatherInfo?> {
                    return dao.queryWeatherByNormal().map { it.firstOrNull() }
                }

                override fun createCall(): LiveData<ApiResponse<WeatherNet>> {
                    return ApiClient.apiService.locationByIPLiveData("json", GD_KEY)
                        .switchMap { ip ->
                            if (ip is ApiSuccessResponse) {
                                ApiClient.apiService.getWeatherByLiveData(getWeatherParams(ip.body))
                            } else {
                                MutableLiveData<ApiResponse<WeatherNet>>()
                                    .apply {
                                        value =
                                            ApiResponse.create(Throwable("location fail"))
                                    }
                            }
                        }
                }

                override fun saveCallResult(item: WeatherNet) {
                    item.lives?.firstOrNull()?.let {
                        dao.insertNormal(it)
                    }
                }

                override fun shouldFetch(data: WeatherInfo?): Boolean {
                    return data == null || rateLimiter.shouldFetch(data.createTime)
                }
            }

        return boundResource.asLiveData()
    }


    //获取天气请求参数
    private fun getWeatherParams(locationByIP: IPLocation): HashMap<String, String> {
        val params = hashMapOf<String, String>()
        params["key"] = GD_KEY
        params["city"] = locationByIP.adcode
        params["extensions"] = "base"
        params["output"] = "json"
        return params
    }
}