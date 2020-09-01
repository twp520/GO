package com.colin.go.api

/**
 * create by colin
 * 2020/8/25
 */
class IPLocation : GaodeInfo() {
    var city: String = ""
    var adcode: String = ""
    var province: String = ""

    override fun toString(): String {
        return "IPLocation(city='$city', adcode='$adcode', province='$province')"
    }


}