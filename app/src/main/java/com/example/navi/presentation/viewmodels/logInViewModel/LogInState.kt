package com.example.navi.presentation.viewmodels.logInViewModel

data class LogInState(
    val email: String = "",
    val password: String = "",
    val receivedTokens: Boolean = false
)
