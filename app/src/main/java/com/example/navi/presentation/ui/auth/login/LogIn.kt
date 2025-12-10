package com.example.navi.presentation.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navi.presentation.viewmodels.auth.logInViewModel.LogInAction
import com.example.navi.presentation.viewmodels.auth.logInViewModel.LogInState

@Composable
fun LogIn(
    modifier: Modifier = Modifier,
    state: LogInState,
    onEvent: (LogInAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MaterialTheme.typography.titleLarge
            Text(
                "Sign In with Email",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Text("Your email")
        TextField(value = state.email, onValueChange = {email -> onEvent(LogInAction.TypeInEmail(email))})
        Text("Your password")
        TextField(value = state.password, onValueChange = {password -> onEvent(LogInAction.TypeInPassword(password))})
        Button(onClick = { onEvent(LogInAction.LogIn) }) {
            Text("Log In")
        }
    }
}