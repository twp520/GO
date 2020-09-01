package com.colin.go.util

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * create by colin
 * 2020/9/1
 */
object BindAdapter {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}