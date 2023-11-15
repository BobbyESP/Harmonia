package com.kyant.music.ui.library

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kyant.music.storage.MediaStore
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Stable
class PermissionViewModel(private val application: Application) : AndroidViewModel(application) {

    val permissions = listOfNotNull(
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
            ContextCompat.checkSelfPermission(application, it) == PackageManager.PERMISSION_GRANTED
        }
    )
        private set

    fun processPermissionResult(granted: Boolean) {
        isGranted = granted
        if (granted) {
            viewModelScope.launch(Dispatchers.IO) {
                MediaStore.scan(application)
            }
        }
    }

    fun goToSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", application.packageName, null)
        }
        context.startActivity(intent)
    }
}
