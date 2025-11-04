package com.example.navi.data.remote.dto

import com.squareup.moshi.Json

data class TokenDTO(
    @field:Json("refresh_token")
    val refreshToken: String,
    @field:Json("access_token")
    val accessToken: String
)