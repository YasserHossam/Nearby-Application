package com.yasser.nearby.network.model

data class FoursquareResponse<T>(private val meta:Meta,
                                 private val response:T)

class Meta(private val code:Int,
           private val requestId: String)