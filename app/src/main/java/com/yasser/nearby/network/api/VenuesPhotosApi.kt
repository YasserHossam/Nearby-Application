package com.yasser.nearby.network.api

import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class VenuesPhotosApi private constructor(retrofit: Retrofit) {

    private val api: Api

    init {
        api = retrofit.create(Api::class.java)
    }

    companion object {

        private lateinit var INSTANCE: VenuesPhotosApi

        fun getExploreApiInstance(retrofit: Retrofit): VenuesPhotosApi {
            if (!this::INSTANCE.isInitialized)
                INSTANCE = VenuesPhotosApi(retrofit)
            return INSTANCE
        }
    }

    fun getImages(venueId: String): Single<FoursquareResponse<VenuePhotosResponse>> {
        return api.getVenueImages(venueId)
    }

    interface Api {
        @GET("venues/{VENUE_ID}/photos")
        fun getVenueImages(@Path(value = "VENUE_ID") venueId: String):
                Single<FoursquareResponse<VenuePhotosResponse>>
    }
}