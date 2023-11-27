package com.kyant.music

import android.app.Application
import com.kyant.music.storage.AndroidMediaStore
import com.kyant.music.storage.AndroidResourceManager
import com.kyant.music.storage.mediaStore
import com.kyant.music.storage.resourceManager
import com.kyant.music.util.DataSaver

class HarmoniaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        DataSaver.init(filesDir.absolutePath)
        mediaStore = AndroidMediaStore(this)
        resourceManager = AndroidResourceManager(this)
    }
}
