package com.example.navi.presentation.ui.auth.signInWithEmail

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
import com.example.navi.presentation.viewmodels.signInViewModel.SignInAction
import com.example.navi.presentation.viewmodels.signInViewModel.SignInState

@Composable
fun EmailVerification(
    modifier: Modifier = Modifier,
    state: SignInState,
    onEvent: (SignInAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Verification Code")
        TextField(value = state.verificationCode, onValueChange = {
            newValue -> onEvent(SignInAction.TypeInVerificationCode(newValue))
        })
        Button(onClick = { onEvent(SignInAction.VerifyCode) }) { Text("Verify") }
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
        state = SignInState(verificationCode = "1234")
    ) { }
}