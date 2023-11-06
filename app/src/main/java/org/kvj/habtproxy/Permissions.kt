package org.kvj.habtproxy

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
            PackageManager.PERMISSION_GRANTED
}
fun Context.hasRequiredRuntimePermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        hasPermission("android.permission.BLUETOOTH_SCAN")
    } else {
        hasPermission("android.permission.ACCESS_FINE_LOCATION")
    }
}

fun Activity.requestRelevantRuntimePermissions() {
    if (hasRequiredRuntimePermissions()) { return }
    when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
            requestLocationPermission()
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            requestBluetoothPermissions()
        }
    }
}

private val RUNTIME_PERMISSION_REQUEST_CODE = 1

private fun Activity.requestLocationPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
        RUNTIME_PERMISSION_REQUEST_CODE
    )
}

private fun Activity.requestBluetoothPermissions() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf("android.permission.BLUETOOTH_SCAN"),
        RUNTIME_PERMISSION_REQUEST_CODE
    )
}