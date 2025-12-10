package com.example.navi.presentation.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object AuthenticationScreen: NavKey, Route {

        @Serializable
        data object WelcomeScreen: NavKey, Route

        @Serializable
        data object SignInWithGoogleScreen: NavKey, Route

        @Serializable
        data object SignInWithEmailScreen: NavKey, Route {
            
            @Serializable
            data object FillInDataScreen: NavKey, Route
            
            @Serializable
            data object VerificationCodeScreen: NavKey, Route
                    
        }

        @Serializable
        data object LogInScreen: NavKey, Route

    }

    @Serializable
    data object DisabledRequestsScreen: NavKey, Route

    @Serializable
    data object DisabledProfileScreen: NavKey, Route
}