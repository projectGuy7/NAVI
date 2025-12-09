package com.example.navi.presentation.locationController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update


data class Location(
    val latitude: Double,
    val longitude: Double
)

object LocationController {

    private val _location = MutableStateFlow(Location(latitude = 43.238949, longitude = 76.889709))
    val location = _location.asStateFlow()

    fun updateLocation(location: Location) {
        _location.value = location
    }

}