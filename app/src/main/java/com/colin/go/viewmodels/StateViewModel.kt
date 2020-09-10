package com.colin.go.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.go.R
import com.colin.go.bean.CODE_NOT_GPS
import com.colin.go.bean.UIResult
import com.colin.go.bean.StateInfo
import com.colin.go.util.Logger

class StateViewModel(app: Application) : AndroidViewModel(app), LocationListener {

    private val _stateInfo: MutableLiveData<UIResult<StateInfo>> = MutableLiveData()
    val stateInfo: LiveData<UIResult<StateInfo>> = _stateInfo


    @SuppressLint("MissingPermission")
    fun startLocation() {
        val locationManager =
            getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (providerEnabled) {
            _stateInfo.value = UIResult.loading()
            val bestProvider = locationManager.getBestProvider(getCriteria(), true) ?: ""
            Logger.d("bestProvider = $bestProvider")
            locationManager.requestLocationUpdates(bestProvider, 1000, 10f, this)
        } else {
            _stateInfo.value = UIResult.error(
                CODE_NOT_GPS,
                getApplication<Application>().getString(R.string.error_disable_gps)
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        val locationManager =
            getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
        Logger.i("onCleared")
    }

    private fun getCriteria(): Criteria {
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isAltitudeRequired = true
        criteria.isBearingRequired = true//包含方位信息
        criteria.isSpeedRequired = true//包含速度信息
        criteria.isCostAllowed = true
        return criteria
    }

    override fun onLocationChanged(location: Location?) {
        //根据经纬度，请求接口，拿到城市。
        //经纬度获取了有正负。经度 西经为-，东经为+。纬度 南纬为-，北纬为+.
        location?.let {
            Logger.d("location provider = ${it.provider}")
            val info = StateInfo(
                it.latitude, it.longitude, it.altitude,
                it.speed * 3.6f, "", it.bearing
            )
            _stateInfo.value = UIResult.content(info)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {
    }
}