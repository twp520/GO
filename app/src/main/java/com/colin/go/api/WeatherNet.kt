package com.colin.go.api

import com.colin.go.bean.WeatherInfo

/**
 * create by colin
 * 2020/8/25
 */
class WeatherNet : GaodeInfo() {

    var lives: MutableList<WeatherInfo>? = null
}