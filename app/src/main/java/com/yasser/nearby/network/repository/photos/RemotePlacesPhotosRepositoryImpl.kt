package com.yasser.nearby.network.repository.photos

import com.google.gson.Gson
import com.yasser.nearby.network.RemoteInvalidCredentialsException
import com.yasser.nearby.network.RemoteNoResultsException
import com.yasser.nearby.network.ServerErrors.Companion.INVALID_AUTH_ERROR
import com.yasser.nearby.network.api.photos.PlacesPhotosApi
import com.yasser.nearby.network.model.FoursquareResponse
import com.yasser.nearby.network.model.PhotoItem
import com.yasser.nearby.network.model.VenuePhotosResponse
import io.reactivex.Single
import retrofit2.HttpException

class RemotePlacesPhotosRepositoryImpl(private val photosApi: PlacesPhotosApi) :
    RemotePlacesPhotosRepository {
    override fun getVenuePhotos(venueId: String): Single<PhotoItem> {
        return photosApi.getImages(venueId)
            .map { getVenuePhotoOrException(it.response) }
            .onErrorResumeNext { throwable ->
                if (throwable is HttpException && throwable.response()?.errorBody() != null) {
                    val errorBody = throwable.response()?.errorBody()
                    val stringResponse = errorBody?.string()
                    val errorResponse =
                        Gson().fromJson(stringResponse, FoursquareResponse::class.java)
                    if (errorResponse.meta.errorType == INVALID_AUTH_ERROR)
                        Single.error(RemoteInvalidCredentialsException())
                    else
                        Single.error(throwable)
                } else
                    Single.error(throwable)
            }
    }

    private fun getVenuePhotoOrException(response: VenuePhotosResponse): PhotoItem {
        if (response.photos.count > 0 && response.photos.items.isNotEmpty())
            return response.photos.items[0]
        else
            throw RemoteNoResultsException()
    }
}