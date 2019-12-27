package com.yasser.nearby.network.model

data class VenuePhotosResponse(val photos: Photo)

data class Photo(
    val count: Int,
    val items: List<PhotoItem>
)

data class PhotoItem(
    val prefix: String,
    val postfix: String
)