package com.example.navi.presentation.ui.navigation.authNavigation.signInNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.navi.presentation.ui.auth.signInWithEmail.EmailVerification
import com.example.navi.presentation.ui.auth.signInWithEmail.SignInWithEmail
import com.example.navi.presentation.ui.navigation.Route
import com.example.navi.presentation.viewmodels.auth.signInViewModel.SignInViewModel

@Composable
fun SignInNavigation(
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel
) {
    val backStack = rememberNavBackStack<NavKey>(Route.AuthenticationScreen.SignInWithEmailScreen.FillInDataScreen)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is Route.AuthenticationScreen.SignInWithEmailScreen.FillInDataScreen -> {
                    NavEntry(key) {
                        SignInWithEmail(
                            modifier = modifier,
                            state = signInViewModel.state,
                            onEvent = signInViewModel::onEvent,
                            transitionToScreen = { backStack.add(Route.AuthenticationScreen.SignInWithEmailScreen.VerificationCodeScreen) }
                        )
                    }
                }
                is Route.AuthenticationScreen.SignInWithEmailScreen.VerificationCodeScreen -> {
                    NavEntry(key) {
                        EmailVerification(
                            modifier = modifier,
                            state = signInViewModel.state,
                            onEvent = signInViewModel::onEvent
                        )
                    }
                }
                else -> {
                    throw IllegalArgumentException("NavKey not identified")
                }
            }
        }
    )
}