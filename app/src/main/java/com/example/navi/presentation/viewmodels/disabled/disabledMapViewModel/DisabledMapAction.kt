package com.example.navi.presentation.viewmodels.disabled.disabledMapViewModel

import com.example.navi.domain.navi.VolunteerLocation

sealed class DisabledMapAction {
    class ShowVolunteerDetails(val volunteerLocation: VolunteerLocation): DisabledMapAction()
    class FindVolunteersInRadius(val radius: Int): DisabledMapAction()
}