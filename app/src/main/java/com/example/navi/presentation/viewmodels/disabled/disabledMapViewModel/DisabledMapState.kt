package com.example.navi.presentation.viewmodels.disabled.disabledMapViewModel

import com.example.navi.domain.navi.VolunteerLocation

data class DisabledMapState(
    val radius: Int = 1,
    val volunteersLocations: List<VolunteerLocation> = emptyList(),
)