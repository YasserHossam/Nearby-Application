package com.yasser.nearby.network.repository.photos

import com.yasser.nearby.network.model.PhotoItem
import io.reactivex.Single

interface RemotePhotosRepository {
    fun getVenuePhotos(venueId: String): Single<PhotoItem>
}