package com.kyant.music.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.music.R
import com.kyant.music.service.PermissionManager
import com.kyant.ui.BottomSheetDialog
import com.kyant.ui.Icon
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.style.color.PerceptualColor
import com.kyant.ui.style.color.ProvideEmphasis
import com.kyant.ui.style.color.animateColorSet
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography

@Composable
fun WelcomeDialog() {
    val context = LocalContext.current
    val permissionManager = remember(context) { PermissionManager(context) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = !permissionManager.isGranted
    }

    BottomSheetDialog(
        visible = visible,
        onDismissRequest = { visible = false },
        dismissable = permissionManager.isGranted
    ) {
        Surface(
            colorSet = colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Welcome to",
                        style = typography.titleLarge
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = colorScheme.primary.color,
                        style = typography.headlineLarge
                    )
                }
                Column(
                    modifier = Modifier.clip(Rounding.Large.asSmoothRoundedShape()),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    val requestPermissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { state ->
                        permissionManager.processPermissionResult(granted = state.all { it.value })
                    }
                    Surface(
                        onClick = { permissionManager.grant(requestPermissionLauncher) },
                        colorSet = colorScheme.surfaceContainerLow
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (permissionManager.isGranted) {
                                    Icons.Default.CheckCircleOutline
                                } else {
                                    Icons.Default.ErrorOutline
                                },
                                tint = animateColorAsState(
                                    targetValue = if (permissionManager.isGranted) {
                                        PerceptualColor.Green.colorScheme.primary.color
                                    } else {
                                        colorScheme.error.color
                                    }
                                ).value
                            )
                            AnimatedContent(
                                targetState = permissionManager.isGranted,
                                modifier = Modifier.weight(1f)
                            ) { isGranted ->
                                if (isGranted) {
                                    SingleLineText("Permissions granted")
                                } else {
                                    SingleLineText("Grant permissions")
                                }
                            }
                        }
                    }
                    Surface(
                        onClick = {},
                        colorSet = colorScheme.surfaceContainerLow
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PrivacyTip,
                                tint = PerceptualColor.Brown.colorScheme.primary.color
                            )
                            SingleLineText(
                                "Privacy policy",
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.Link,
                                emphasis = 0.6f
                            )
                        }
                    }
                    Surface(
                        onClick = {},
                        colorSet = colorScheme.surfaceContainerLow
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.NewReleases,
                                tint = PerceptualColor.Orange.colorScheme.primary.color
                            )
                            SingleLineText(
                                "What's new",
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.Link,
                                emphasis = 0.6f
                            )
                        }
                    }
                    Surface(
                        onClick = { visible = false },
                        colorSet = animateColorSet(
                            if (permissionManager.isGranted) {
                                colorScheme.surfaceContainerLowest
                            } else {
                                colorScheme.surfaceDim
                            }
                        ),
                        enabled = permissionManager.isGranted
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProvideEmphasis(
                                if (permissionManager.isGranted) 1f else 0.6f,
                                if (permissionManager.isGranted) 1f else 0.5f
                            ) {
                                Icon(imageVector = Icons.Outlined.Done)
                                SingleLineText(
                                    "Done",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
