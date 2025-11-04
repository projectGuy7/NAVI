package com.example.navi.presentation.snackBarController

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackBarEvent(
    val message: String,
    val action: SnackBarAction? = null
)

data class SnackBarAction(
    val name: String,
    val action: suspend () -> Unit
)

object SnackBarController {

    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(snackBarEvent: SnackBarEvent) {
        _events.send(snackBarEvent)
    }
}