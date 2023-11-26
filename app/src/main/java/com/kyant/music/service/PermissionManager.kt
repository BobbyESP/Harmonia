package com.kyant.music.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import kotlinx.collections.immutable.toImmutableList

@Stable
class PermissionManager(private val context: Context) {

    private val permissions = listOfNotNull(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            null
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        }
    ).toImmutableList()

    var isGranted by mutableStateOf(
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    )
        private set

    fun processPermissionResult(granted: Boolean) {
        isGranted = granted
    }

    fun launchAppInfo() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun grant(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(permissions.toTypedArray())
    }
}
