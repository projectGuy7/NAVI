package com.example.navi.data.mappers

import com.example.navi.data.remote.dto.DisabledDTO
import com.example.navi.data.remote.dto.TokenDTO
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.navi.Token

fun TokenDTO.toToken() : Token {
    return Token(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken
    )
}

fun DisabledDTO.toDisabled() : Disabled {
    return Disabled(
        firstName = this.firstName,
        lastName = this.lastName,
        age = this.age.toByte(),
        email = this.email,
        problems = this.problems
    )
}