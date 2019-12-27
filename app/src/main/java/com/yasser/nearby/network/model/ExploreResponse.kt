package com.yasser.nearby.network.model

data class ExploreResponse(private val groups: List<Group>)

data class Group(
    private val type: String,
    private val name: String,
    private val items: List<GroupItem>
)

data class GroupItem(private val venues: List<Venue>)

data class Venue(
    private val id: String,
    private val name: String,
    private val location: FoursquareLocation
)

data class FoursquareLocation(private val formattedAddress: List<String>)