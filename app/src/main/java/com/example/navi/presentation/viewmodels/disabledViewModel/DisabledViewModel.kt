package com.example.navi.presentation.viewmodels.disabledViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.di.factories.DisabledViewModelFactory
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.navi.Disabled
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource
import com.example.navi.presentation.viewmodels.EXPIRED_REFRESH_TOKEN
import com.example.navi.presentation.viewmodels.UNAUTHORIZED_ACCESS
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File


@HiltViewModel(assistedFactory = DisabledViewModelFactory::class)
class DisabledViewModel @AssistedInject constructor(
    @Assisted val disabledRepository: DisabledRepository,
    @param:FilesDir val filesDir: File
): ViewModel() {

    var state by mutableStateOf(DisabledState())
        private set

    fun onEvent() {
        viewModelScope.launch {
            when(val result = disabledRepository.me()) {
                is Resource.Error<*> -> {
                    Log.i("me()", "${result.message}")
                    if(result.responseCode == UNAUTHORIZED_ACCESS) {

                    }
                }
                is Resource.Success<Disabled> -> {
                    state = state.copy(
                        firstName = result.data?.firstName ?: "Empty",
                        lastName = result.data?.lastName ?: "Empty",
                        age = result.data?.age ?: -1,
                        email = result.data?.email ?: "Empty",
                        problems = result.data?.problems ?: "Empty"
                    )
                }
            }
        }
    }

    private fun disposeApi(refreshToken: String) {
        viewModelScope.launch {
            when(val result = disabledRepository.refreshToken(RefreshTokenDTO(refreshToken))) {
                is Resource.Success -> {
                    disabledRepository.disposeApi(accessToken = result.data?.accessToken!!)
                }
                is Resource.Error -> {
                    if(result.responseCode == EXPIRED_REFRESH_TOKEN) {
                        // TODO
                    }
                }
            }
        }
    }
}