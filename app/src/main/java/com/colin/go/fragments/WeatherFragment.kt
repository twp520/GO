package com.colin.go.fragments

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.colin.go.BaseFragment
import com.colin.go.R
import com.colin.go.databinding.WeatherFragmentBinding
import com.colin.go.viewmodels.WeatherViewModel

class WeatherFragment : BaseFragment<WeatherFragmentBinding>() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun getContentLayoutId(): Int {
        return R.layout.weather_fragment
    }

    override fun initView(contentView: View) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        /* viewModel.weatherData.observe(viewLifecycleOwner, Observer {
             if (it.isContent()) {
                 binding.weatherText.text = it.content?.toString() ?: "null"
             }
         })*/

//        viewModel.weatherDataNormal.observe(viewLifecycleOwner, Observer {
//            binding.weatherLoading.isVisible = it.isLoading()
//            if (it.isContent()) {
//                binding.weatherText.text = it.content?.toString() ?: "null"
//            }
//        })


    }

}