package com.example.navi.presentation.ui.disabled.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.navi.presentation.viewmodels.disabled.disabledViewModel.DisabledState

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: DisabledState,
    onEvent: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val fullName = "${state.firstName} ${state.lastName}"
        ProfileHeaderElevatedCard(fullName) { }
        PersonalInformationCard(fullName = fullName, age = "${state.age}", email = state.email)
    }
}

@Composable
fun ProfileHeaderElevatedCard(
    name: String,
    onEditClicked: () -> Unit
) {
    // Using ElevatedCard as requested
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.padding(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Edit Button
                IconButton(onClick = onEditClicked) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Name Only (No "Member since")
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun PersonalInformationCard(
    fullName: String,
    age: String,
    email: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Full Name Row
            PersonalInfoRow(
                icon = Icons.Default.Person,
                label = "Full Name",
                value = fullName
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Age Row
            PersonalInfoRow(
                icon = Icons.Default.Build,
                label = "Age",
                value = age
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Email Row
            PersonalInfoRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = email
            )
        }
    }
}

@Composable
fun PersonalInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(
    apiLevel = 35,
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        state = DisabledState(firstName = "John", lastName = "Smith", age = 34, email = "johnsmith@gmail.com"),
        {}
    )
}