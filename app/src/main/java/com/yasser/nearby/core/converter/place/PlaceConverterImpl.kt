package com.yasser.nearby.core.converter.place

import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.network.model.FoursquareLocation
import com.yasser.nearby.network.model.PhotoItem
import com.yasser.nearby.network.model.Venue

class PlaceConverterImpl : PlaceConverter {

    companion object {
        private const val IMAGE_SIZE = "300x300"
    }

    override fun convertFromNetworkToCore(venue: Venue, photoItem: PhotoItem?): AppPlace {
        return AppPlace(
            venue.id,
            venue.name,
            extractAddress(venue.location),
            extractImageUrl(photoItem)
        )
    }

    private fun extractAddress(location: FoursquareLocation): String {
        return location.formattedAddress.joinToString("\n")
    }

    private fun extractImageUrl(photoItem: PhotoItem?): String? {
        return photoItem?.prefix + IMAGE_SIZE + photoItem?.postfix
    }
}