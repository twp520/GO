package com.colin.go.util

import android.util.Log

/**
 * create by colin
 * 2020/9/1
 */
object Logger {

    const val tag = "goLogger"

    fun i(msg: String) {
        Log.i(tag, msg)
    }

    fun d(msg: String) {
        Log.d(tag, msg)
    }

    fun e(msg: String) {
        Log.e(tag, msg)
    }
}