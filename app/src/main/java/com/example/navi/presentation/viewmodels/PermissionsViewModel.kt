package com.example.navi.presentation.viewmodels

import android.Manifest
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {
    val permissionQueue = mutableStateListOf<String>()
    val grantedPermissions = mutableStateMapOf<String, Boolean>(
        Pair(Manifest.permission.ACCESS_FINE_LOCATION, false)
    )

    fun popLast() {
        permissionQueue.removeAt(0)
    }

    fun onPermissionResult(isGranted: Boolean, permission: String) {
        if(!isGranted && !permissionQueue.contains(permission)) {
            permissionQueue.add(permission)
        } else if(isGranted) {
            grantedPermissions[permission] = true
            Log.i("2", "$permission , $grantedPermissions")
        }
    }
}