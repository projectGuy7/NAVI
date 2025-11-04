package com.example.navi.presentation.ui.util

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack.reset(key: NavKey) {
    this.clear()
    this.add(key)
}