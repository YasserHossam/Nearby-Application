package com.yasser.nearby.ui.location

import android.app.Activity
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

class NearbyLocationManagerImpl(
    activity: Activity,
    private val locationUpdateDistance: Int
) : NearbyLocationManager {

    private var fusedLocationClient: FusedLocationProviderClient?

    private val nearbyLocationManagerCallback: NearbyLocationManagerCallback

    private lateinit var locationCallback: LocationCallback

    private var lastValidLocation: Location? = null

    init {
        if (activity is NearbyLocationManagerCallback)
            nearbyLocationManagerCallback = activity
        else
            throw RuntimeException(
                "Any Activity that use LocationManager must implement " +
                        "LocationManagerCallback"
            )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    override fun getLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            it?.let {
                nearbyLocationManagerCallback.onLocationFetched(it)
                lastValidLocation = it
            }
        }
    }

    override fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            getLocationCallback(),
            Looper.getMainLooper()
        )
    }

    override fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(getLocationCallback())
    }

    override fun onDestroy() {
        fusedLocationClient?.removeLocationUpdates(getLocationCallback())
        fusedLocationClient = null
    }

    private fun getLocationCallback(): LocationCallback {
        if (!this::locationCallback.isInitialized)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    val currentLocation = locationResult?.lastLocation
                    if (currentLocation != null && lastValidLocation != null) {
                        if (currentLocation.distanceTo(lastValidLocation) >= locationUpdateDistance) {
                            nearbyLocationManagerCallback.onUpdatedLocationFetched(currentLocation)
                            lastValidLocation = currentLocation
                        }
                    }
                }
            }

        return locationCallback
    }
}