package com.example.navi.data.remote.dto

data class RequestDTO(
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val time: String,
    val day: String,
    val place: String
)
