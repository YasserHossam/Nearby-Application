package com.yasser.nearby.network.api.explore

import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.FoursquareResponse
import io.reactivex.Single

interface ExplorePlacesApi {
    fun explore(
        latLong: String,
        radius: Int
    ): Single<FoursquareResponse<ExploreResponse>>
}