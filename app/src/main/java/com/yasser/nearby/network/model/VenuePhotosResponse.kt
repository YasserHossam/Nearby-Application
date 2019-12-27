package com.yasser.nearby.network.model

data class VenuePhotosResponse(private val photos: Photo)

data class Photo(private val count: Int,
                 private val items: List<PhotoItem>)

data class PhotoItem(private val prefix: String,
                     private val postfix: String)