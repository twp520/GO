package com.colin.go.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * create by colin
 * 2020/8/19
 *
 * 天气信息UI层实体类
 */

@Entity(tableName = "t_weather")
class WeatherInfo {

    var province: String = ""
    var city: String = ""
    var adcode: String = ""

    var createTime: Long = System.currentTimeMillis()

    @PrimaryKey
    var id: Long = 1

    //天气现象（汉字描述）
    var weather: String = ""

    //实时气温，单位：摄氏度
    var temperature: String = ""

    //风向描述
    var winddirection: String = ""

    //风力级别，单位：级
    var windpower: String = ""

    //空气湿度
    var humidity: String = ""

    //发布时间
    var reporttime: String = ""
    override fun toString(): String {
        return "WeatherInfo(province='$province', city='$city', adcode='$adcode', id=$id," +
                " weather='$weather', temperature='$temperature', winddirection='$winddirection" +
                "', windpower='$windpower', humidity='$humidity', reporttime='$reporttime , createTime = $createTime')"
    }


}