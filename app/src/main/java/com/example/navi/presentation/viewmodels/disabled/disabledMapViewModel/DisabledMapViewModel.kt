package com.example.navi.presentation.viewmodels.disabled.disabledMapViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource
import com.example.navi.presentation.locationController.LocationController
import com.example.navi.presentation.snackBarController.SnackBarController
import com.example.navi.presentation.snackBarController.SnackBarEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisabledMapViewModel @Inject constructor(val disabledRepository: DisabledRepository): ViewModel() {

    private val _disabledStateFlow = MutableStateFlow(DisabledMapState())
    val disabledStateFlow = _disabledStateFlow.asStateFlow()

    fun onEvent(action: DisabledMapAction) {
        when(action) {
            is DisabledMapAction.ShowVolunteerDetails -> {
                viewModelScope.launch {
                    when(val result = disabledRepository.searchInRadius(
                        _disabledStateFlow.value.radius,
                        LocationController.location.value.run {
                            LocationDTO(
                                longitude = longitude,
                                latitude = latitude
                            )
                        }
                    )) {
                        is Resource.Success -> {
                            _disabledStateFlow.update {
                                it.copy(volunteersLocations = result.data!!)
                            }
                        }
                        is Resource.Error -> {
                            SnackBarController.sendEvent(SnackBarEvent(message = result.message ?: "DisabledViewModel: line 40"))
                        }
                    }
                }
            }
            is DisabledMapAction.FindVolunteersInRadius -> {

            }
        }
    }

}