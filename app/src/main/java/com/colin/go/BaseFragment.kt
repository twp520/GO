package com.colin.go

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * create by colin
 * 2020/8/19
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, getContentLayoutId(), container, false)
        logInfo("onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    abstract fun getContentLayoutId(): Int

    abstract fun initView(contentView: View)

    override fun onStop() {
        super.onStop()
        logInfo("stop")
    }

    override fun onStart() {
        super.onStart()
        logInfo("start")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logInfo("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logInfo("onDestroy")
    }

    protected fun logInfo(msg: String) {
        Log.i(javaClass.name, msg)
    }

    protected fun logError(msg: String) {
        Log.e(javaClass.name, msg)
    }
}