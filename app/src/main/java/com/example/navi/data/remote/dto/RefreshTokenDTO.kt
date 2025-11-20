package com.example.navi.data.remote.dto

import com.squareup.moshi.Json

data class RefreshTokenDTO(
    @field:Json("refresh_token")
    val refreshToken: String
)
