package com.colin.go.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.colin.go.bean.UIResult
import com.colin.go.bean.WeatherInfo
import com.colin.go.repository.WeatherRepository

class WeatherViewModel(app: Application) : AndroidViewModel(app) {


    private val repository = WeatherRepository(app.applicationContext)
//    val weatherData: MediatorLiveData<Result<WeatherInfo?>> = MediatorLiveData()

//    val weatherDataNormal: LiveData<UIResult<WeatherInfo>> = repository.getWeatherByNormal()

    val weatherDataLiveData: LiveData<UIResult<WeatherInfo?>> =
        repository.getWeatherByBoundResource()

    override fun onCleared() {
        super.onCleared()
        Log.e("WeatherViewModel", "onCleared")
    }

    /*init {
        weatherData.also {
            it.addSource(repository.data) { result ->
                weatherData.value = result
            }
        }
    }*/


    /*fun getWeatherInfoBySuspend() {
        viewModelScope.launch {
            //这里通过应该okhttp拦截器，对返回结果做统一处理，并抛出异常。
            try {
                repository.getWeatherBySuspend()
            } catch (e: Exception) {
                e.printStackTrace()
                weatherData.value = Result.error(CODE_ERROR, "exception error")
            }
        }
    }*/

}