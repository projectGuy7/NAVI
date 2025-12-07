package com.example.navi.di.factories

import androidx.navigation3.runtime.NavBackStack
import com.example.navi.presentation.viewmodels.disabledViewModel.DisabledViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface DisabledViewModelFactory {

    fun create(backStack: NavBackStack): DisabledViewModel

}