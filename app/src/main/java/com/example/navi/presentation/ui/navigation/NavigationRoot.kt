package com.example.navi.presentation.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.navi.di.factories.DisabledViewModelFactory
import com.example.navi.presentation.ui.disabled.profile.ProfileScreen
import com.example.navi.presentation.ui.auth.WelcomeScreen
import com.example.navi.presentation.ui.auth.login.LogIn
import com.example.navi.presentation.ui.auth.signInWithEmail.EmailVerification
import com.example.navi.presentation.ui.auth.signInWithEmail.SignInWithEmail
import com.example.navi.presentation.ui.util.reset
import com.example.navi.presentation.viewmodels.disabled.disabledViewModel.DisabledViewModel
import com.example.navi.presentation.viewmodels.logInViewModel.LogInViewModel
import com.example.navi.presentation.viewmodels.signInViewModel.SignInViewModel
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeScreen: NavKey

@Serializable
data object SignInWithEmailScreen: NavKey

@Serializable
data object LogInScreen: NavKey

@Serializable
data object HomeScreen: NavKey


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    backStack: NavBackStack
) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                WelcomeScreen -> {
                    NavEntry(key) {
                        WelcomeScreen(
                            modifier = modifier,
                            onSignInWithGooglePressed = {}, // TODO
                            onSignInWithEmailPressed = {
                                backStack.add(SignInWithEmailScreen)
                            },
                            onAlreadyHaveAnAccountPressed = {
                                backStack.add(LogInScreen)
                            }
                        )
                    }
                }
                SignInWithEmailScreen -> {
                    NavEntry(key) {
                        val signInViewModel = hiltViewModel<SignInViewModel>()
                        val state = signInViewModel.state
                        if(state.receivedTokens) {
                            backStack.reset(HomeScreen)
                        }
                        if(!state.sentVerificationCode) {
                            SignInWithEmail(
                                modifier = modifier,
                                state = state,
                                onEvent = signInViewModel::onEvent,
                                onBackClicked = { backStack.removeAt(backStack.size - 1) }
                            )
                        } else {
                            EmailVerification(
                                modifier = modifier,
                                state = state,
                                onEvent = signInViewModel::onEvent
                            )
                        }
                    }
                }
                LogInScreen -> {
                    NavEntry(key) {
                        val logInViewModel = hiltViewModel<LogInViewModel>()
                        val state = logInViewModel.state
                        if(state.receivedTokens) {
                            backStack.reset(HomeScreen)
                        } else {
                            LogIn(
                                modifier = Modifier.fillMaxSize().padding(5.dp),
                                state = state,
                                onEvent = logInViewModel::onEvent,
                                onBackClicked = { backStack.removeAt(backStack.size - 1) }
                            )
                        }
                    }
                }
                is HomeScreen -> {
                    NavEntry(key) {
                        val disabledViewModel: DisabledViewModel = hiltViewModel<DisabledViewModel, DisabledViewModelFactory>(
                            creationCallback = { factory ->
                                factory.create(backStack)
                            }
                        )
                        ProfileScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            state = disabledViewModel.state,
                            onEvent = disabledViewModel::onEvent
                        )
                    }
                }
                else -> throw Exception("NavKey not identified")
            }
        }
    )
}