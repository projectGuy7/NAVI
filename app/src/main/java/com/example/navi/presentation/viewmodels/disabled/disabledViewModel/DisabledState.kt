package com.example.navi.presentation.viewmodels.disabled.disabledViewModel

import com.example.navi.domain.navi.Request

data class DisabledState(
    val firstName: String = "",
    val lastName: String = "",
    val age: Byte = 0,
    val email: String = "",
    val problems: String = "",

    val requests: List<Request> = emptyList()
)