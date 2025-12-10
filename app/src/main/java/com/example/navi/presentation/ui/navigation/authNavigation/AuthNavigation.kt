package com.example.navi.presentation.ui.navigation.authNavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.navi.presentation.ui.auth.WelcomeScreen
import com.example.navi.presentation.ui.auth.login.LogIn
import com.example.navi.presentation.ui.navigation.Route
import com.example.navi.presentation.ui.navigation.authNavigation.signInNavigation.SignInNavigation
import com.example.navi.presentation.ui.util.reset
import com.example.navi.presentation.viewmodels.auth.logInViewModel.LogInViewModel
import com.example.navi.presentation.viewmodels.auth.signInViewModel.SignInViewModel

@Composable
fun AuthNavigation(
    modifier: Modifier = Modifier,
    onSuccessfullyAuthenticated: () -> Unit
) {
    val backStack = rememberNavBackStack<NavKey>(Route.AuthenticationScreen.WelcomeScreen)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is Route.AuthenticationScreen.WelcomeScreen -> {
                    NavEntry(key) {
                        WelcomeScreen(
                            modifier = modifier,
                            onSignInWithGooglePressed = {},
                            onSignInWithEmailPressed = { backStack.add(Route.AuthenticationScreen.SignInWithEmailScreen) },
                            onAlreadyHaveAnAccountPressed = { backStack.add(Route.AuthenticationScreen.LogInScreen) }
                        )
                    }
                }
                is Route.AuthenticationScreen.SignInWithEmailScreen -> {
                    NavEntry(key) {
                        val signInViewModel = hiltViewModel<SignInViewModel>()
                        val state = signInViewModel.state
                        SignInNavigation(
                            modifier = modifier,
                            signInViewModel = signInViewModel
                        )
                        if(state.receivedTokens) onSuccessfullyAuthenticated()
//                        if(!state.sentVerificationCode) {
//                            SignInWithEmail(
//                                modifier = modifier,
//                                state = state,
//                                onEvent = signInViewModel::onEvent,
//                                onBackClicked = { backStack.removeAt(backStack.size - 1) }
//                            )
//                        } else {
//                            EmailVerification(
//                                modifier = modifier,
//                                state = state,
//                                onEvent = signInViewModel::onEvent
//                            )
//                        }
                    }
                }
                is Route.AuthenticationScreen.LogInScreen -> {
                    NavEntry(key) {
                        val logInViewModel = hiltViewModel<LogInViewModel>()
                        val state = logInViewModel.state
                        if(state.receivedTokens) {
                            onSuccessfullyAuthenticated()
                        } else {
                            LogIn(
                                modifier = modifier,
                                state = state,
                                onEvent = logInViewModel::onEvent
                            )
                        }
                    }
                }
                else -> {
                    throw throw IllegalArgumentException("NavKey not identified")
                }
            }
        }
    )
}