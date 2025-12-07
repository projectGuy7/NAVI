package com.example.navi.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.navi.R

@Composable
fun NaviBottomAppBar(
    modifier: Modifier = Modifier,
    onMapPressed: () -> Unit,
    onMyRequestsPressed: () -> Unit,
    onProfilePressed: () -> Unit
) {
    
    BottomAppBar(
        modifier = modifier,
        actions = {
            BottomBarIcon(
                modifier = Modifier.weight(1f),
                imageVector = ImageVector.vectorResource(R.drawable.map),
                description = "Map",
                onClick = onMapPressed
            )
            BottomBarIcon(
                modifier = Modifier.weight(1f),
                imageVector = Icons.AutoMirrored.Filled.List,
                description = "Requests",
                onClick = onMyRequestsPressed
            )
            BottomBarIcon(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Default.Person,
                description = "Profile",
                onClick = onProfilePressed
            )
        }
    )
}

@Composable
fun BottomBarIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClick ) {
            Icon(
                imageVector = imageVector,
                contentDescription = description
            )
        }
        Text(description)
    }
}

@Preview(
    apiLevel = 35,
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun NaviBottomAppBarPreview() {
    return Scaffold(
        bottomBar = { NaviBottomAppBar(
            onMapPressed = {},
            onProfilePressed = {},
            onMyRequestsPressed = {}
        ) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {

        }
    }
}