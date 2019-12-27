package com.yasser.nearby.network.model

class ExploreResponse(groups: List<Group>)

class Group(
    private val type: String,
    private val name: String,
    private val items: List<GroupItem>
)

class GroupItem(private val venues: List<Venue>)

class Venue(
    private val id: String,
    private val name: String,
    private val location: FoursquareLocation
)

class FoursquareLocation(private val formattedAddress: List<String>)