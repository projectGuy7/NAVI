package com.example.navi.data.remote.dto

import com.squareup.moshi.Json

data class UserDTO(
    @field:Json("first_name")
    val firstName: String,
    @field:Json("last_name")
    val lastName: String,
    val age: Byte,
    val email: String,
    val password: String,
    val role: String,
    val problems: String? = null
)
