package com.example.navi.presentation.viewmodels.disabled.disabledViewModel

import com.example.navi.domain.navi.Request

sealed class DisabledAction {
    data class AddToRequestsList(val request: Request): DisabledAction()
}