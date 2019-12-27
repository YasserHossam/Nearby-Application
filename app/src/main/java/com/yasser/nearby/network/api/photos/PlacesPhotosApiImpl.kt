package com.yasser.nearby.network.api.photos

import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class PlacesPhotosApiImpl private constructor(retrofit: Retrofit): PlacesPhotosApi {

    private val api: Api

    init {
        api = retrofit.create(Api::class.java)
    }

    companion object {

        private lateinit var INSTANCE: PlacesPhotosApiImpl

        fun getExploreApiInstance(retrofit: Retrofit): PlacesPhotosApiImpl {
            if (!this::INSTANCE.isInitialized)
                INSTANCE =
                    PlacesPhotosApiImpl(
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