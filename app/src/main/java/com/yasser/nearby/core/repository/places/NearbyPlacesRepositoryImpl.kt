package com.yasser.nearby.core.repository.places

import com.yasser.nearby.core.InvalidCredentialsException
import com.yasser.nearby.core.NetworkException
import com.yasser.nearby.core.NoResultsException
import com.yasser.nearby.core.converter.place.PlaceConverter
import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.network.RemoteInvalidCredentialsException
import com.yasser.nearby.network.RemoteNetworkException
import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.model.Venue
import com.yasser.nearby.network.repository.photos.RemotePlacesPhotosRepository
import com.yasser.nearby.network.repository.places.RemotePlacesRepository
import io.reactivex.Observable

class NearbyPlacesRepositoryImpl(
    private val remotePlacesRepository: RemotePlacesRepository,
    private val remotePlacesPhotosRepository: RemotePlacesPhotosRepository,
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
                    is RemoteInvalidCredentialsException ->
                        Observable.error(InvalidCredentialsException())
                    else -> Observable.error(throwable)
                }
            }
    }

    private fun getPhotosObservable(venue: Venue): Observable<AppPlace> {
        return remotePlacesPhotosRepository.getVenuePhotos(venue.id)
            .toObservable()
            .map { photoItem ->
                placeConverter.convertFromNetworkToCore(venue, photoItem)
            }
            .onErrorReturn {
                placeConverter.convertFromNetworkToCore(venue)
            }
    }
}