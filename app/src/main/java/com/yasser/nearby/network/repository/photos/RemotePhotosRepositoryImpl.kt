package com.yasser.nearby.network.repository.photos

import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.api.photos.VenuesPhotosApi
import com.yasser.nearby.network.model.PhotoItem
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single

class RemotePhotosRepositoryImpl(private val photosApi: VenuesPhotosApi) : RemotePhotosRepository {
    override fun getVenuePhotos(venueId: String): Single<PhotoItem> {
        return photosApi.getImages(venueId)
            .map { getVenuePhotoOrException(it.response) }
    }

    private fun getVenuePhotoOrException(response: VenuePhotosResponse): PhotoItem {
        if (response.photos.count > 0 && response.photos.items.isNotEmpty())
            return response.photos.items[0]
        else
            throw RemoteNoResultsException()
    }
}