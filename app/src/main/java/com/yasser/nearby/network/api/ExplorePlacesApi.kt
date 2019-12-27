package com.yasser.nearby.network.api

import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.FoursquareResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class ExplorePlacesApi private constructor(retrofit: Retrofit) {

    private val api: Api

    init {
        api = retrofit.create(Api::class.java)
    }

    companion object {

        private lateinit var INSTANCE: ExplorePlacesApi

        fun getExploreApiInstance(retrofit: Retrofit): ExplorePlacesApi {
            if (!this::INSTANCE.isInitialized)
                INSTANCE = ExplorePlacesApi(retrofit)
            return INSTANCE
        }
    }

    fun explore(
        latLong: String,
        radius: Int,
        limit: Int
    ): Single<FoursquareResponse<ExploreResponse>> {
        return api.explorePlaces(latLong, radius, limit)
    }


    private interface Api {
        @GET("venues/explore?")
        fun explorePlaces(
            @Query("ll") latLong: String,
            @Query("radius") searchRadius: Int,
            @Query("limit") resultsLimit: Int
        ): Single<FoursquareResponse<ExploreResponse>>
    }
}