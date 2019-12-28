package com.yasser.nearby.network.model

import com.google.gson.annotations.SerializedName

data class VenuePhotosResponse(val photos: Photo)

data class Photo(
    @SerializedName("count")
    val count: Int,

    @SerializedName("items")
    val items: List<PhotoItem>
)

data class PhotoItem(
    @SerializedName("prefix")
    val prefix: String,

    @SerializedName("suffix")
    val suffix: String
)