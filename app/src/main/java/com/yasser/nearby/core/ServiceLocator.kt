package com.yasser.nearby.core

import android.content.Context
import com.yasser.nearby.core.converter.place.PlaceConverterImpl
import com.yasser.nearby.core.repository.places.NearbyPlacesRepository
import com.yasser.nearby.core.repository.places.NearbyPlacesRepositoryImpl
import com.yasser.nearby.core.utils.getConfigString
import com.yasser.nearby.network.api.RetrofitProvider
import com.yasser.nearby.network.api.explore.ExplorePlacesApiImpl
import com.yasser.nearby.network.api.photos.PlacesPhotosApiImpl
import com.yasser.nearby.network.repository.photos.RemotePlacesPhotosRepositoryImpl
import com.yasser.nearby.network.repository.places.RemotePlacesRepositoryImpl

class ServiceLocator private constructor(context: Context) {

    private val nearbyPlacesRepository: NearbyPlacesRepository

    init {
        // Get retrofit dependencies from config.xml
        val baseUrl = context.getConfigString("base_url")
        val clientId = context.getConfigString("client_id")
        val clientSecret = context.getConfigString("client_secret")
        val foursquareApiVersion = context.getConfigString("foursquare_api_version")

        val retrofit = RetrofitProvider.getRetrofitInstance(
            baseUrl,
            clientId,
            clientSecret,
            foursquareApiVersion
        )

        val explorePlacesApi = ExplorePlacesApiImpl.getExploreApiInstance(retrofit)
        val remotePlacesRepository = RemotePlacesRepositoryImpl(explorePlacesApi)

        val photosApi = PlacesPhotosApiImpl.getExploreApiInstance(retrofit)
        val remotePlacesPhotosRepository = RemotePlacesPhotosRepositoryImpl(photosApi)

        nearbyPlacesRepository = NearbyPlacesRepositoryImpl(
            remotePlacesRepository,
            remotePlacesPhotosRepository,
            PlaceConverterImpl()
        )
    }

    fun getNearbyPlacesRepository(): NearbyPlacesRepository = nearbyPlacesRepository

    companion object {

        private var sInstance: ServiceLocator? = null

        fun initializeController(context: Context) {
            if (sInstance == null) {
                synchronized(ServiceLocator::class.java) {
                    if (sInstance == null)
                        sInstance = ServiceLocator(context)
                }
            }
        }

        fun getInstance(): ServiceLocator {
            if (sInstance == null) {
                throw RuntimeException("ServiceLocator must be initialized first in application class")
            } else
                return sInstance!!
        }
    }
}