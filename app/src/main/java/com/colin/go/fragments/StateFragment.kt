package com.colin.go.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.colin.go.BaseFragment
import com.colin.go.R
import com.colin.go.bean.StateInfo
import com.colin.go.databinding.StateFragmentBinding
import com.colin.go.viewmodels.StateViewModel

class StateFragment : BaseFragment<StateFragmentBinding>() {


    private val viewModel: StateViewModel by viewModels()

    override fun getContentLayoutId(): Int {
        return R.layout.state_fragment
    }

    override fun initView(contentView: View) {
        val find = ContextCompat.checkSelfPermission(
            contentView.context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarse = ContextCompat.checkSelfPermission(
            contentView.context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val background = ContextCompat.checkSelfPermission(
            contentView.context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        if (find == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED
            && background == PackageManager.PERMISSION_GRANTED
        ) {
            //初始化
            initStateInfo()
        } else {
            //请求权限
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1000
            )
        }

    }

    private fun initStateInfo() {
        viewModel.stateInfo.observe(viewLifecycleOwner, Observer {
            binding.stateLoading.isVisible = it.isLoading()
            if (it.isContentState()) {
                setContent(it.content!!)
            }
        })
        viewModel.startLocation()
    }

    private fun setContent(content: StateInfo) {
        binding.stateSpeed.text = getString(R.string.string_speed, content.speed.toString())
        binding.stateBearing.text = getString(R.string.string_bearing, content.bearing.toString())
        binding.stateAltitude.text =
            getString(R.string.string_altitude, content.altitude.toString())
        binding.stateLatLong.text = buildString {
            append(
                if (content.lat > 0) getString(
                    R.string.string_north_lat,
                    content.lat.toString()
                ) else
                    getString(R.string.string_south_lat, content.lat.toString())
            )
            append(",")
            append(
                if (content.long > 0) getString(
                    R.string.string_east_lon,
                    content.long.toString()
                ) else
                    getString(R.string.string_west_lon, content.long.toString())
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1000 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            //初始化
            initStateInfo()
        }
    }
}