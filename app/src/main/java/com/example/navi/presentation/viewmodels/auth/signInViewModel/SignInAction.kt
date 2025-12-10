package com.example.navi.presentation.viewmodels.auth.signInViewModel

sealed class SignInAction {
    data class TypeInFirstName(val firstName: String): SignInAction()
    data class TypeInLastName(val lastName: String): SignInAction()
    data class ChooseYearOfBirth(val yearOfBirth: Int): SignInAction()
    data class TypeInEmail(val email: String): SignInAction()
    data class TypeInPassword(val password: String): SignInAction()
    data object ChangeRole: SignInAction()
    data class TypeInProblems(val problems: String): SignInAction()
    data class CreateUser(val transitionToScreen: () -> Unit): SignInAction()
    data class TypeInVerificationCode(val verificationCode: String): SignInAction()
    data object VerifyCode: SignInAction()
}