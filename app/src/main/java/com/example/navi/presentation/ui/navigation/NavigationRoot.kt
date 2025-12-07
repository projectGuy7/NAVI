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
import com.example.navi.presentation.ui.login.WelcomeScreen
import com.example.navi.presentation.ui.login.signInWithEmail.EmailVerification
import com.example.navi.presentation.ui.login.signInWithEmail.SignInWithEmail
import com.example.navi.presentation.ui.util.reset
import com.example.navi.presentation.viewmodels.disabledViewModel.DisabledViewModel
import com.example.navi.presentation.viewmodels.loginViewmodel.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeScreen: NavKey

@Serializable
data object LoginWithEmailScreen: NavKey

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
                                backStack.add(LoginWithEmailScreen)
                            },
                            onAlreadyHaveAnAccountPressed = {} // TODO
                        )
                    }
                }
                LoginWithEmailScreen -> {
                    NavEntry(key) {
                        val loginViewModel = hiltViewModel<LoginViewModel>()
                        val state = loginViewModel.loginState
                        if(state.receivedTokens) {
                            backStack.reset(HomeScreen)
                        }
                        if(!state.sentVerificationCode) {
                            SignInWithEmail(
                                modifier = modifier,
                                loginState = state,
                                onEvent = loginViewModel::onEvent,
                                onBackClicked = { backStack.removeAt(backStack.size - 1) }
                            )
                        } else {
                            EmailVerification(
                                modifier = modifier,
                                state = state,
                                onEvent = loginViewModel::onEvent
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