package com.example.navi.presentation.viewmodels.disabled.disabledViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import com.example.navi.cryptoManager.TokenCryptoManager
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.di.factories.DisabledViewModelFactory
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource
import com.example.navi.presentation.snackBarController.SnackBarController
import com.example.navi.presentation.snackBarController.SnackBarEvent
import com.example.navi.presentation.ui.navigation.Route
import com.example.navi.presentation.ui.util.reset
import com.example.navi.presentation.viewmodels.EXPIRED_TOKEN
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


@HiltViewModel(assistedFactory = DisabledViewModelFactory::class)
class DisabledViewModel @AssistedInject constructor(
    val disabledRepository: DisabledRepository,
    @param:FilesDir val filesDir: File,
    @Assisted val backStack: NavBackStack
): ViewModel() {

    var state by mutableStateOf(DisabledState())
        private set

    init {
        viewModelScope.launch {
            when(val result = disabledRepository.me()) {
                is Resource.Success<Disabled> -> {
                    state = state.copy(
                        firstName = result.data?.firstName ?: "Empty",
                        lastName = result.data?.lastName ?: "Empty",
                        age = result.data?.age ?: -1,
                        email = result.data?.email ?: "Empty",
                        problems = result.data?.problems ?: "Empty"
                    )
                }
                is Resource.Error<*> -> {
                    Log.i("me()", "${result.message}")
                    if(result.responseCode == EXPIRED_TOKEN) {
                        disposeApi()
                    }
                }
            }
        }
    }

    fun onEvent(action: DisabledAction) {
        when(action) {
            is DisabledAction.AddToRequestsList -> {
                // TODO
            }
        }
    }

    private fun disposeApi() {
        val cryptoManager = TokenCryptoManager()
        val refreshToken = cryptoManager.decrypt(
            FileInputStream(File(filesDir, "token.txt"))
        ).refreshToken
        viewModelScope.launch {
            when(val result = disabledRepository.refreshToken(RefreshTokenDTO(refreshToken))) {
                is Resource.Success -> {
                    SnackBarController.sendEvent(SnackBarEvent(message = "refreshToken() Success"))
                    disabledRepository.disposeApi(accessToken = result.data?.accessToken!!)
                }
                is Resource.Error -> {
                    Log.i("Hey", "disposeApi() Error")
                    SnackBarController.sendEvent(SnackBarEvent(message = result.message ?: "disposeApi() Error"))
                    if(result.responseCode == EXPIRED_TOKEN) {
                        backStack.reset(Route.AuthenticationScreen)
                    }
                }
            }
        }
    }
}