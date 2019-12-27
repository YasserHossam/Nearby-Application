package com.yasser.nearby.network.model

data class FoursquareResponse<T>(
    val meta: Meta,
    val response: T
)

data class Meta(
    val code: Int,
    val requestId: String
)