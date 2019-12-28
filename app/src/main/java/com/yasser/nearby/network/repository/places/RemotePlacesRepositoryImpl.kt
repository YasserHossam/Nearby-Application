package com.yasser.nearby.network.repository.places

import com.google.gson.Gson
import com.yasser.nearby.network.RemoteInvalidCredentialsException
import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.ServerErrors.Companion.INVALID_AUTH_ERROR
import com.yasser.nearby.network.api.explore.ExplorePlacesApi
import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.Venue
import io.reactivex.Single
import retrofit2.HttpException

class RemotePlacesRepositoryImpl(private val explorePlacesApi: ExplorePlacesApi) :
    RemotePlacesRepository {

    override fun getNearbyPlaces(
        latLong: String,
        radius: Int
    ): Single<List<Venue>> {
        return explorePlacesApi.explore(latLong, radius)
            .map { getVenuesOrException(it.response) }
            .onErrorResumeNext { throwable ->
                if (throwable is HttpException && throwable.response()?.errorBody() != null) {
                    val errorBody = throwable.response()?.errorBody()
                    val stringResponse = errorBody?.string()
                    val errorResponse =
                        Gson().fromJson(stringResponse, FoursquareResponse::class.java)
                    if (errorResponse.meta.errorType == INVALID_AUTH_ERROR)
                        Single.error(RemoteInvalidCredentialsException())
                    else
                        Single.error(throwable)
                } else
                    Single.error(throwable)
            }
    }

    private fun getVenuesOrException(response: ExploreResponse): List<Venue> {
        if (response.groups.isNotEmpty()) {
            return response.groups[0].items.map { item -> item.venue }
        } else
            throw RemoteNoResultsException()
    }
}