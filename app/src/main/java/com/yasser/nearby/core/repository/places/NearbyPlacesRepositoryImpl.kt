package com.yasser.nearby.core.repository.places

import com.yasser.nearby.core.NetworkException
import com.yasser.nearby.core.NoResultsException
import com.yasser.nearby.core.converter.place.PlaceConverter
import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.network.RemoteNetworkException
import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.model.Venue
import com.yasser.nearby.network.repository.photos.RemotePhotosRepository
import com.yasser.nearby.network.repository.places.RemotePlacesRepository
import io.reactivex.Observable

class NearbyPlacesRepositoryImpl(
    private val remotePlacesRepository: RemotePlacesRepository,
    private val remotePhotosRepository: RemotePhotosRepository,
    private val placeConverter: PlaceConverter
) : NearbyPlacesRepository {

    override fun getNearbyPlaces(
        latLong: String,
        radiusInMeters: Int
    ): Observable<AppPlace> {
        return remotePlacesRepository
            .getNearbyPlaces(latLong, radiusInMeters)
            .flatMapObservable { venuesList ->
                Observable.fromIterable(venuesList)
                    .flatMap { venue ->
                        getPhotosObservable(venue)
                    }
            }.onErrorResumeNext { throwable: Throwable ->
                when (throwable) {
                    is RemoteNetworkException -> Observable.error(NetworkException())
                    is RemoteNoResultsException -> Observable.error(NoResultsException())
                    else -> Observable.error(throwable)
                }
            }
    }

    private fun getPhotosObservable(venue: Venue): Observable<AppPlace> {
        return remotePhotosRepository.getVenuePhotos(venue.id)
            .toObservable()
            .map { photoItem ->
                placeConverter.convertFromNetworkToCore(venue, photoItem)
            }
            .onErrorReturn {
                placeConverter.convertFromNetworkToCore(venue)
            }
    }
}