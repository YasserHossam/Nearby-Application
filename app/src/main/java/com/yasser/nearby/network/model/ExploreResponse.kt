package com.yasser.nearby.network.model

import com.google.gson.annotations.SerializedName

data class ExploreResponse(
    @SerializedName("groups")
    val groups: List<Group>
)

data class Group(
    @SerializedName("type")
    val type: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("items")
    val items: List<GroupItem>
)

data class GroupItem(
    @SerializedName("venue")
    val venue: Venue
)

data class Venue(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("location")
    val location: FoursquareLocation
)

data class FoursquareLocation(
    @SerializedName("formattedAddress")
    val formattedAddress: List<String>
)