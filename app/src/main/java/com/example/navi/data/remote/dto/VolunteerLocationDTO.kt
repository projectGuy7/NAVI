package com.example.navi.data.remote.dto

import com.squareup.moshi.Json

data class VolunteerLocationDTO(
    val longitude: Double,
    val latitude: Double,
    @field:Json("user_id")
    val userId: Int,
    val distance: Int
)
