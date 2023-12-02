package com.kyant.music

import android.app.Application
import com.kyant.music.config.ConfigStore
import com.kyant.music.config.configStore
import com.kyant.music.storage.AndroidMediaStore
import com.kyant.music.storage.AndroidResourceManager
import com.kyant.music.storage.mediaStore
import com.kyant.music.storage.resourceManager
import com.kyant.music.util.DataSaver

class HarmoniaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        DataSaver.init(filesDir.absolutePath)
        configStore = ConfigStore(filesDir.absolutePath, "config")
        mediaStore = AndroidMediaStore(this)
        resourceManager = AndroidResourceManager(this)
    }
}
