package com.example.navi.presentation.ui.disabledMainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navi.presentation.viewmodels.disabledViewModel.DisabledState

@Composable
fun DisabledMainScreen(
    modifier: Modifier = Modifier,
    state: DisabledState,
    onEvent: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "name: ${state.firstName}"
        )
        Text(
            text = state.lastName
        )
        Text(
            text = state.email
        )
        Text(
            text = "${state.age}"
        )
        Text(
            text = state.problems
        )
        Button(
            onClick = onEvent
        ) {
            Text("PRESS ME")
        }
    }
}