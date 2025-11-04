package com.example.navi.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navi.R
import com.example.navi.ui.theme.NAVITheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onSignInWithGooglePressed: () -> Unit,
    onSignInWithEmailPressed: () -> Unit,
    onAlreadyHaveAnAccountPressed: () -> Unit
) {
    val logoFontFamily = FontFamily(Font(R.font.righteous_regular))
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.project_logo_name),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 60.sp,
                    fontFamily = logoFontFamily
                )
                Text(
                    stringResource(R.string.project_logo_moto),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 22.sp,
                    fontFamily = logoFontFamily
                )
            }

        }
        Column(
            modifier = Modifier.weight(3f).padding(bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Bottom)
        ) {
            Button(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = onSignInWithGooglePressed
            ) {
                Text("Sign In using Google account")
            }
            Button(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = onSignInWithEmailPressed
            ) {
                Text("Sign In using Email")
            }
            Row(
                modifier = Modifier.padding(top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("Already have an Account?")
                Text(
                    "Log In",
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = onAlreadyHaveAnAccountPressed
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun PreviewWelcomeScreen() {
    NAVITheme {
        WelcomeScreen(
            onSignInWithGooglePressed = {},
            onSignInWithEmailPressed = {},
            onAlreadyHaveAnAccountPressed = {}
        )
    }
}