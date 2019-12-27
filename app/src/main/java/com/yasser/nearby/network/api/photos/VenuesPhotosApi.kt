package com.yasser.nearby.network.api.photos

import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single

interface VenuesPhotosApi {
    fun getImages(venueId: String): Single<FoursquareResponse<VenuePhotosResponse>>
}