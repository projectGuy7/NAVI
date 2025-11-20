package com.example.navi.di.factories

import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.presentation.viewmodels.disabledViewModel.DisabledViewModel
import dagger.assisted.AssistedFactory
import java.io.File

@AssistedFactory
interface DisabledViewModelFactory {
    fun create(disabledRepository: DisabledRepository): DisabledViewModel
}