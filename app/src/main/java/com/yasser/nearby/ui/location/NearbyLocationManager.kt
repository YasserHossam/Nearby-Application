package com.yasser.nearby.ui.location

interface NearbyLocationManager {
    fun getLocation()

    fun startLocationUpdates()

    fun stopLocationUpdates()

    fun onDestroy()
}