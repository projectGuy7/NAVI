package com.example.navi.data.remote.dto

import com.squareup.moshi.Json
data class EmailVerificationDTO(
    val email: String,
    @field:Json("verification_code")
    val verificationCode: String
)
