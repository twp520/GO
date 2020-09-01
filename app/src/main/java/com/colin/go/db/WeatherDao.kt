package com.colin.go.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.colin.go.bean.WeatherInfo

/**
 * create by colin
 * 2020/8/25
 */

@Dao
interface WeatherDao {

    @Query("select * from t_weather")
    suspend fun queryWeather(): List<WeatherInfo>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuspend(info: WeatherInfo)


    @Query("select * from t_weather")
    fun queryWeatherByNormal(): LiveData<List<WeatherInfo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNormal(info: WeatherInfo)

}