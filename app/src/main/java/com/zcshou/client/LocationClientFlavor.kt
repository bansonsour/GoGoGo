package com.zcshou.client

import android.util.Log
import com.baidu.mapapi.model.LatLng
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.map.geolocation.TencentLocationRequest.REQUEST_LEVEL_POI

/**
 * @Author zhouy
 * @Date 2020-09-22
 */

private const val RECENT_VALID_TIME = 15   // 最近位置仅15分钟内有效
private const val TAG = "LocationClientFlavor"

object LocationClientFlavor {

    fun init() {
        
    }

    private val mTencentLocationClient by lazy {
        TencentLocationClient()
    }

    fun start() {
        mTencentLocationClient.start()
    }

    fun stop() {
        mTencentLocationClient.stop()
    }
}


/**
 * 腾讯定位Client
 */
private class TencentLocationClient : TencentLocationListener {
    private var mWorking: Boolean = false
    private var mLocationClient: TencentLocationManager? = null
    private val mRequest by lazy {
        TencentLocationRequest.create().apply {
            interval = 3000
            requestLevel = REQUEST_LEVEL_POI
            isAllowGPS = true
            isIndoorLocationMode = true
        }
    }

    fun start() {
        try {
            stop()
            Log.i(TAG, "TencentLocationClient start")
            mWorking = true
            mLocationClient = TencentLocationManager.getInstance(RuntimeInfo.sAppContext)
            val res = mLocationClient?.requestLocationUpdates(mRequest, this@TencentLocationClient)
            Log.e(TAG, "TencentLocationClient requestLocationUpdates res=$res")
        } catch (e: Exception) {
            mWorking = false
            Log.e(TAG, "TencentLocationClient start failed")
        }
    }

    override fun onLocationChanged(loc: TencentLocation?, error: Int, reason: String?) {
        val msg = reason ?: ""
        val location = if (error == 0 && loc != null) {

            val bdCoordinate = gcj02ToBd09(loc.latitude, loc.longitude)
            val nation = (loc.nation ?: "中国").replace("Unknown", "")
            val province = (loc.province ?: "").replace("Unknown", "")
            val city = (loc.city ?: "").replace("Unknown", "")
            val district = (loc.district ?: "").replace("Unknown", "")
            val town = ((loc.town ?: "") + (loc.village ?: "")).replace("Unknown", "")
            val street = (loc.street ?: "").replace("Unknown", "")
            val streetNo = (loc.streetNo ?: "").replace("Unknown", "")
            val address = (loc.address ?: "").replace("Unknown", "")

            val addressStr = if (address.isEmpty()) {
                "$province$city$district$town$street$streetNo"
            } else {
                address
            }

        } else {
            null
        }

        val locationType = loc?.provider ?: ""
        val locationTypeDesc = getLocationTypeDesc(locationType)
        Log.i(TAG, "腾讯定位结果 code=$error msg=$msg " +
                " locationType=$locationTypeDesc location=$location loc=$loc")
    }

    private fun getLocationTypeDesc(locationType: String): String {
        return when (locationType) {
            TencentLocation.GPS_PROVIDER -> "GPS定位"
            TencentLocation.NETWORK_PROVIDER -> "网络定位"
            else -> "未知"
        }
    }

    override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
        Log.e(TAG, "TencentLocationClient onStatusUpdate name=$name status=$status desc=$desc")
    }

    fun stop() {
        mLocationClient?.removeUpdates(this)
        mLocationClient = null
        mWorking = false
    }
}

/**
 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
 *
 * @param latitude
 * @param longitude
 */
private fun gcj02ToBd09(latitude: Double, longitude: Double): Pair<Double, Double> {
    val desLatLng = com.baidu.mapapi.utils.CoordinateConverter()
            .from(com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON)
            .coord(LatLng(latitude, longitude)).convert()
    return Pair(desLatLng.latitude, desLatLng.longitude)
}