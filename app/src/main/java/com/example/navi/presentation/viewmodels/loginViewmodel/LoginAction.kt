package com.example.navi.presentation.viewmodels.loginViewmodel

sealed class LoginAction {
    data class TypeInFirstName(val firstName: String): LoginAction()
    data class TypeInLastName(val lastName: String): LoginAction()
    data class ChooseYearOfBirth(val yearOfBirth: Int): LoginAction()
    data class TypeInEmail(val email: String): LoginAction()
    data class TypeInPassword(val password: String): LoginAction()
    data object ChangeRole: LoginAction()
    data class TypeInProblems(val problems: String): LoginAction()
    data object CreateUser: LoginAction()
    data class TypeInVerificationCode(val verificationCode: String): LoginAction()
    data object VerifyCode: LoginAction()
}