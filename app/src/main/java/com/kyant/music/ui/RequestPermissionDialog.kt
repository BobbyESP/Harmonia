package com.kyant.music.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kyant.music.service.PermissionManager
import com.kyant.ui.Button
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.TextButton
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun RequestPermissionDialog() {
    val context = LocalContext.current
    val manager = remember(context) { PermissionManager(context) }
    if (manager.isGranted) return

    AlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures() }
                .systemBarsPadding()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = SmoothRoundedCornerShape(32.dp),
                colorToken = Theme.colorScheme.surfaceContainer
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "⚠️ Permission required",
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = Theme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "You need to grant the permissions to use the app.",
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = Theme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                    TextButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    ) {
                        Text(text = "Privacy policy")
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                    TextButton(
                        onClick = { manager.launchAppInfo() },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    ) {
                        Text(text = "App info")
                    }
                    val requestPermissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { state ->
                        manager.processPermissionResult(granted = state.all { it.value })
                    }
                    Button(
                        onClick = { requestPermissionLauncher.launch(manager.permissions.toTypedArray()) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Continue",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
