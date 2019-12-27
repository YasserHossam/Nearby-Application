package com.yasser.nearby.network.repository.places

import com.yasser.nearby.network.NetworkNoResultsException
import com.yasser.nearby.network.api.explore.ExplorePlacesImplementation
import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.Venue
import io.reactivex.Single

class RemotePlacesRepositoryImplementation(
    private val explorePlacesApi: ExplorePlacesImplementation
) : RemotePlacesRepository {

    override fun getNearbyPlaces(
        latLong: String,
        radius: Int,
        limit: Int
    ): Single<List<Venue>> {
        return explorePlacesApi.explore(latLong, radius, limit)
            .map { getVenuesOrException(it.response) }
    }

    private fun getVenuesOrException(response: ExploreResponse): List<Venue> {
        if (response.groups.isNotEmpty()) {
            return response.groups[0].items.map { item -> item.venue }
        } else
            throw NetworkNoResultsException()
    }
}