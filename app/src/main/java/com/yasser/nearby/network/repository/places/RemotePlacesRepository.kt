package com.yasser.nearby.network.repository.places

import com.yasser.nearby.network.model.Venue
import io.reactivex.Single

interface RemotePlacesRepository {
    fun getNearbyPlaces(
        latLong: String,
        radius: Int,
        limit: Int
    ): Single<List<Venue>>
}