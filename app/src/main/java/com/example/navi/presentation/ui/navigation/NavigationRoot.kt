package com.example.navi.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.navi.data.remote.api.createDisabledApi
import com.example.navi.data.remote.interceptor.AuthorizationInterceptor
import com.example.navi.data.repository.DisabledRepositoryImpl
import com.example.navi.di.factories.DisabledViewModelFactory
import com.example.navi.presentation.ui.disabledMainScreen.DisabledMainScreen
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
    modifier: Modifier = Modifier
) {
    var backStack = rememberNavBackStack<NavKey>(WelcomeScreen)
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
                        val disabledViewModel: DisabledViewModel = hiltViewModel(
                            creationCallback = { factory: DisabledViewModelFactory ->
                                factory.run {
                                    create(DisabledRepositoryImpl(createDisabledApi(AuthorizationInterceptor(
                                        key.accessToken,
                                    ))))
                                }
                            }
                        )
                        DisabledMainScreen(
                            modifier = modifier,
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