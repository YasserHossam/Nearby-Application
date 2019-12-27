package com.yasser.nearby.core.converter.place

import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.network.model.PhotoItem
import com.yasser.nearby.network.model.Venue

interface PlaceConverter {
    fun convertFromNetworkToCore(venue: Venue, photoItem: PhotoItem? = null): AppPlace
}