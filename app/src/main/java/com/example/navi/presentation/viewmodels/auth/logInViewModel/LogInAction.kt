package com.example.navi.presentation.viewmodels.auth.logInViewModel

sealed class LogInAction {
    data class TypeInEmail(val email: String): LogInAction()
    data class TypeInPassword(val password: String): LogInAction()
    data object LogIn: LogInAction()
}