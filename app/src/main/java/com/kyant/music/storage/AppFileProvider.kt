package com.kyant.music.storage

import androidx.core.content.FileProvider

class AppFileProvider : FileProvider() {
    companion object {
        const val AUTHORITY = "com.kyant.music.storage.provider"
    }
}
