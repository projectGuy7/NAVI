package com.example.navi.presentation.locationController

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


data class Location(
    val latitude: Double,
    val longitude: Double
)

object LocationController {

    private val _locations = Channel<Location>()

    val locations = _locations.receiveAsFlow()

    suspend fun sendLocation(location: Location) {
        _locations.send(location)
    }

}