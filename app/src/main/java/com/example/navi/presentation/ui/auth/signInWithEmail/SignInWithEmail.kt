package com.example.navi.presentation.ui.auth.signInWithEmail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.navi.presentation.viewmodels.signInViewModel.SignInAction
import com.example.navi.presentation.viewmodels.signInViewModel.SignInState
import com.example.navi.ui.theme.NAVITheme

@Composable
fun SignInWithEmail(
    modifier: Modifier = Modifier,
    state: SignInState,
    onEvent: (action: SignInAction) -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            MaterialTheme.typography.titleLarge
            Text(
                "Sign In with Email",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Text("First Name")
        TextField(value = state.firstName, onValueChange = { newValue -> onEvent(SignInAction.TypeInFirstName(newValue))})
        Text("Last Name")
        TextField(value = state.lastName, onValueChange = { newValue -> onEvent(SignInAction.TypeInLastName(newValue))})
        Text("Age")
        TextField(value = state.age.toString(), onValueChange = { newValue -> onEvent(SignInAction.ChooseYearOfBirth(2025 - newValue.toInt()))})
        Text("Email")
        TextField(value = state.email, onValueChange = { newValue -> onEvent(SignInAction.TypeInEmail(newValue))})
        Text("Password")
        TextField(value = state.password, onValueChange = { newValue -> onEvent(SignInAction.TypeInPassword(newValue))})
        Text("Volunteer or Disabled")
        Row(modifier = Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(if(state.isVolunteer) "Volunteer" else "Disabled")
            Button(onClick = {onEvent(SignInAction.ChangeRole)}) { Text("Change") }
        }
        Button(onClick = {onEvent(SignInAction.CreateUser)}) { Text("Create User") }
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun SignInWithEmailPreview() {
    NAVITheme {
        SignInWithEmail(modifier = Modifier.fillMaxSize().padding(5.dp), onEvent = {}, onBackClicked = {}, state = SignInState())
    }
}