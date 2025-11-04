package com.example.navi.presentation.ui.login.signInWithEmail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.navi.presentation.viewmodels.loginViewmodel.LoginAction
import com.example.navi.presentation.viewmodels.loginViewmodel.LoginState

@Composable
fun EmailVerification(
    modifier: Modifier = Modifier,
    state: LoginState,
    onEvent: (LoginAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Verification Code")
        TextField(value = state.verificationCode, onValueChange = { newValue -> onEvent(LoginAction.TypeInVerificationCode(newValue)) })
        Button(onClick = { onEvent(LoginAction.VerifyCode) }) { Text("Verify") }
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun EmailVerificationPreview() {
    EmailVerification(
        state = LoginState(verificationCode = "1234")
    ) { }
}