package com.yasser.nearby.core.repository.places

import com.yasser.nearby.core.model.AppPlace
import io.reactivex.Observable

interface NearbyPlacesRepository {
    fun getNearbyPlaces(
        latLong: String,
        radiusInMeters: Int
    ): Observable<AppPlace>
}