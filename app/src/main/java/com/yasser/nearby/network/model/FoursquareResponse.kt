package com.yasser.nearby.network.model

import com.google.gson.annotations.SerializedName

data class FoursquareResponse<T>(
    @SerializedName("meta")
    val meta: Meta,

    @SerializedName("response")
    val response: T
)

data class Meta(
    @SerializedName("code")
    val code: Int,

    @SerializedName("requestId")
    val requestId: String,

    @SerializedName("errorType")
    val errorType: String,

    @SerializedName("errorDetail")
    val errorDetails: String
)