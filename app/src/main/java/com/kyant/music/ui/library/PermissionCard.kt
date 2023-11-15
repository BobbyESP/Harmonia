package com.kyant.music.ui.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.ui.Button
import com.kyant.ui.FilledTonalButton
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun PermissionCard() {
    val viewModel = viewModel<PermissionViewModel>()

    if (!viewModel.isGranted) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = SmoothRoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "⚠️ Permission required",
                        style = Theme.typography.titleLarge
                    )
                    Text(
                        text = "You need to grant the permissions to use the app.",
                        style = Theme.typography.bodyMedium
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val context = LocalContext.current
                FilledTonalButton(
                    onClick = { viewModel.goToSettings(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Go to settings",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { state ->
                    viewModel.processPermissionResult(granted = state.all { it.value })
                }
                Button(
                    onClick = { requestPermissionLauncher.launch(viewModel.permissions.toTypedArray()) },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Continue",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
