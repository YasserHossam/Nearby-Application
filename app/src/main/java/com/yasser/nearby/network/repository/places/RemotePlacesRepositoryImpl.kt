package com.yasser.nearby.network.repository.places

import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.api.explore.ExplorePlacesApi
import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.Venue
import io.reactivex.Single

class RemotePlacesRepositoryImpl(private val explorePlacesApi: ExplorePlacesApi) :
    RemotePlacesRepository {

    override fun getNearbyPlaces(
        latLong: String,
        radius: Int
    ): Single<List<Venue>> {
        return explorePlacesApi.explore(latLong, radius)
            .map { getVenuesOrException(it.response) }
    }

    private fun getVenuesOrException(response: ExploreResponse): List<Venue> {
        if (response.groups.isNotEmpty()) {
            return response.groups[0].items.map { item -> item.venue }
        } else
            throw RemoteNoResultsException()
    }
}