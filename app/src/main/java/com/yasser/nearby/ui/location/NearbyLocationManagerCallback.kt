package com.yasser.nearby.ui.location

import android.location.Location

interface NearbyLocationManagerCallback {
    fun onLocationFetched(location: Location)
    fun onUpdatedLocationFetched(location: Location)
}