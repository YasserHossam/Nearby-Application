package com.yasser.nearby.network.api.photos

import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class VenuesPhotosImplementation private constructor(retrofit: Retrofit): VenuesPhotosApi {

    private val api: Api

    init {
        api = retrofit.create(Api::class.java)
    }

    companion object {

        private lateinit var INSTANCE: VenuesPhotosImplementation

        fun getExploreApiInstance(retrofit: Retrofit): VenuesPhotosImplementation {
            if (!this::INSTANCE.isInitialized)
                INSTANCE =
                    VenuesPhotosImplementation(
                        retrofit
                    )
            return INSTANCE
        }
    }

    override fun getImages(venueId: String): Single<FoursquareResponse<VenuePhotosResponse>> {
        return api.getVenueImages(venueId)
    }

    interface Api {
        @GET("venues/{VENUE_ID}/photos")
        fun getVenueImages(@Path(value = "VENUE_ID") venueId: String):
                Single<FoursquareResponse<VenuePhotosResponse>>
    }
}