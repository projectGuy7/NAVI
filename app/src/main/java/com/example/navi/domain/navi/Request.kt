package com.example.navi.domain.navi

import java.time.LocalDateTime

data class Request(
    val description: String,
    val dateTime: LocalDateTime,
    val place: String
)