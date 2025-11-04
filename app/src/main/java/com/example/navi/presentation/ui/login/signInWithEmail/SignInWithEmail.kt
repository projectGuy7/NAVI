package com.example.navi.presentation.ui.login.signInWithEmail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.navi.presentation.viewmodels.loginViewmodel.LoginAction
import com.example.navi.presentation.viewmodels.loginViewmodel.LoginState
import com.example.navi.ui.theme.NAVITheme

@Composable
fun SignInWithEmail(
    modifier: Modifier = Modifier,
    loginState: LoginState,
    onEvent: (action: LoginAction) -> Unit,
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
        TextField(value = loginState.firstName, onValueChange = { newValue -> onEvent(LoginAction.TypeInFirstName(newValue))})
        Text("Last Name")
        TextField(value = loginState.lastName, onValueChange = { newValue -> onEvent(LoginAction.TypeInLastName(newValue))})
        Text("Age")
        TextField(value = loginState.age.toString(), onValueChange = { newValue -> onEvent(LoginAction.ChooseYearOfBirth(2025 - newValue.toInt()))})
        Text("Email")
        TextField(value = loginState.email, onValueChange = { newValue -> onEvent(LoginAction.TypeInEmail(newValue))})
        Text("Password")
        TextField(value = loginState.password, onValueChange = { newValue -> onEvent(LoginAction.TypeInPassword(newValue))})
        Text("Volunteer or Disabled")
        Row(modifier = Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(if(loginState.isVolunteer) "Volunteer" else "Disabled")
            Button(onClick = {onEvent(LoginAction.ChangeRole)}) { Text("Change") }
        }
        Button(onClick = {onEvent(LoginAction.CreateUser)}) { Text("Create User") }
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
        SignInWithEmail(modifier = Modifier.padding(5.dp), onEvent = {}, onBackClicked = {}, loginState = LoginState())
    }
}