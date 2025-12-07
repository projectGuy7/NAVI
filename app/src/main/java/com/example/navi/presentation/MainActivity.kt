package com.example.navi.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.navi.R
import com.example.navi.databinding.ActivityMapsBinding
import com.example.navi.presentation.DisabledLocationService.Companion.ACTION_START
import com.example.navi.presentation.DisabledLocationService.Companion.ACTION_STOP
import com.example.navi.presentation.snackBarController.ObserveAsEvents
import com.example.navi.presentation.snackBarController.SnackBarController
import com.example.navi.presentation.ui.NaviBottomAppBar
import com.example.navi.presentation.ui.PermissionDialog
import com.example.navi.presentation.ui.PreciseLocationPermissionText
import com.example.navi.presentation.ui.navigation.HomeScreen
import com.example.navi.presentation.ui.navigation.NavigationRoot
import com.example.navi.presentation.ui.navigation.WelcomeScreen
import com.example.navi.presentation.viewmodels.PermissionsViewModel
import com.example.navi.ui.theme.NAVITheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val permissionViewModel: PermissionsViewModel by viewModels()
            val dialogQueue = permissionViewModel.permissionQueue

            val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { perms ->
                    permissionsToRequest.forEach { permission ->
                        permissionViewModel.onPermissionResult(
                            isGranted = perms[permission] == true,
                            permission = permission
                        )
                    }
                }
            )

            LaunchedEffect(Unit) {
                multiplePermissionResultLauncher.launch(permissionsToRequest)
            }
            dialogQueue
                .reversed()
                .forEach { permission ->
                    PermissionDialog(
                        permissionTextProvider = when (permission) {
                            Manifest.permission.ACCESS_FINE_LOCATION -> PreciseLocationPermissionText()
                            else -> return@forEach
                        },
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                            permission
                        ),
                        onDismiss = permissionViewModel::popLast,
                        onOkClick = {
                            permissionViewModel.popLast()
                            multiplePermissionResultLauncher.launch(
                                arrayOf(permission)
                            )
                        },
                        onGoToAppSettingsClick = { this.openAppSettings() },
                        modifier = Modifier,
                    )
                }
            checkLocationSettingsResolution()

            val snackBarHostState = remember { SnackbarHostState() }
            ObserveAsEvents(SnackBarController.events) { event ->
                snackBarHostState.currentSnackbarData?.dismiss()

                val result = snackBarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    duration = SnackbarDuration.Long
                )
                if(result == SnackbarResult.ActionPerformed) {
                    event.action?.action?.invoke()
                }
            }
            val backStack = rememberNavBackStack<NavKey>(WelcomeScreen)
            NAVITheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    },
                    bottomBar = if(!backStack.contains(WelcomeScreen)) {
                        if(permissionViewModel.grantedPermissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                            Intent(this@MainActivity, DisabledLocationService::class.java).apply {
                                action = ACTION_START
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(this)
                                } else {
                                    startService(this)
                                }
                            }
                        }
                        { NaviBottomAppBar(
                            onMapPressed = { startActivity(Intent("android.intent.action.MAP")) },
                            onMyRequestsPressed = {},
                            onProfilePressed = { backStack.add(HomeScreen) }
                        ) }
                    } else { {} }
                ) { innerPadding ->
                    NavigationRoot(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Intent(this, DisabledLocationService::class.java).apply {
            action = ACTION_STOP
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(this)
            } else {
                startService(this)
            }
        }
    }

    fun checkLocationSettingsResolution() {
        val locationRequest = LocationRequest.Builder(5000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(3000)
            .build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this,
                        1
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.i("ERROR", "COULDN'T SEND LOCATION SETTING INTENT")
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}