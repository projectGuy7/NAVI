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
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.navi.di.factories.DisabledViewModelFactory
import com.example.navi.presentation.ui.disabled.profile.ProfileScreen
import com.example.navi.presentation.ui.auth.login.LogIn
import com.example.navi.presentation.ui.auth.signInWithEmail.EmailVerification
import com.example.navi.presentation.ui.auth.signInWithEmail.SignInWithEmail
import com.example.navi.presentation.ui.navigation.authNavigation.AuthNavigation
import com.example.navi.presentation.ui.util.reset
import com.example.navi.presentation.viewmodels.disabled.disabledViewModel.DisabledViewModel
import com.example.navi.presentation.viewmodels.auth.logInViewModel.LogInViewModel
import com.example.navi.presentation.viewmodels.auth.signInViewModel.SignInViewModel


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    backStack: NavBackStack
) {
    val disabledViewModel: DisabledViewModel = hiltViewModel<DisabledViewModel, DisabledViewModelFactory>(
        creationCallback = { factory ->
            factory.create(backStack)
        }
    )
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                Route.AuthenticationScreen -> {
                    NavEntry(key) {
                        val onSuccessfullyAuthenticated: () -> Unit = {
                            backStack.remove(Route.AuthenticationScreen)
                            backStack.add(Route.DisabledProfileScreen)
                        }
                        AuthNavigation(
                            modifier = modifier,
                            onSuccessfullyAuthenticated = onSuccessfullyAuthenticated
                        )
                    }
                }
                Route.DisabledProfileScreen -> {
                    NavEntry(key) {
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
                Route.DisabledRequestsScreen -> {
                    NavEntry(key) {

                    }
                }
                else -> throw IllegalArgumentException("NavKey not identified")
            }
        }
    )
}