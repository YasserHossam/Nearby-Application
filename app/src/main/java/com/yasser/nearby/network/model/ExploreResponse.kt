package com.yasser.nearby.network.model

data class ExploreResponse(val groups: List<Group>)

data class Group(
    val type: String,
    val name: String,
    val items: List<GroupItem>
)

data class GroupItem(val venues: List<Venue>)

data class Venue(
    val id: String,
    val name: String,
    val location: FoursquareLocation
)

data class FoursquareLocation(val formattedAddress: List<String>)