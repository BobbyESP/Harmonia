package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.NPBar
import com.kyant.music.ui.style.valueToken
import com.kyant.ui.BoxNoInline

@Composable
fun LibraryNavigator.LibraryContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BoxNoInline(
            modifier = Modifier.weight(1f)
        ) {
            when (listPaneRoute) {
                ListPaneRoute.Songs -> Songs()
                ListPaneRoute.Albums -> Albums()
                else -> {}
            }
        }

        NPBar(
            modifier = Modifier.padding(bottom = valueToken.safeBottomPadding.value)
        )
    }
}
