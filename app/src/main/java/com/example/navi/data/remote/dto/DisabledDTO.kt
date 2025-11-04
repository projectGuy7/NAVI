package com.example.navi.data.remote.dto

import com.squareup.moshi.Json

data class DisabledDTO(
    @field:Json("first_name")
    val firstName: String,
    @field:Json("last_name")
    val lastName: String,
    val age: Int,
    val email: String,
    val problems: String
)
