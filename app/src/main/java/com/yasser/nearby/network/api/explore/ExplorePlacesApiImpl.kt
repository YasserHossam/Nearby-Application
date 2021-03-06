package com.yasser.nearby.network.api.explore

import com.yasser.nearby.network.model.ExploreResponse
import com.yasser.nearby.network.model.FoursquareResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class ExplorePlacesApiImpl private constructor(retrofit: Retrofit) : ExplorePlacesApi {

    private val api: Api

    init {
        api = retrofit.create(Api::class.java)
    }

    companion object {

        private lateinit var INSTANCE: ExplorePlacesApiImpl

        fun getExploreApiInstance(retrofit: Retrofit): ExplorePlacesApiImpl {
            if (!this::INSTANCE.isInitialized)
                INSTANCE =
                    ExplorePlacesApiImpl(
                        retrofit
                    )
            return INSTANCE
        }
    }

    override fun explore(
        latLong: String,
        radius: Int
    ): Single<FoursquareResponse<ExploreResponse>> {
        return api.explorePlaces(latLong, radius)
    }


    private interface Api {
        @GET("venues/explore?")
        fun explorePlaces(
            @Query("ll") latLong: String,
            @Query("radius") searchRadius: Int
        ): Single<FoursquareResponse<ExploreResponse>>
    }
}