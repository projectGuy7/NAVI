package com.example.navi.presentation.viewmodels.loginViewmodel

data class LoginState(
    val firstName: String = "",
    val lastName: String = "",
    val age: Byte = 0,
    val email: String = "",
    val password: String = "",
    val isVolunteer: Boolean = false,
    val problems: String? = null,

    val sentVerificationCode: Boolean = false,
    val verificationCode: String = "",

    val receivedTokens: Boolean = false
)
