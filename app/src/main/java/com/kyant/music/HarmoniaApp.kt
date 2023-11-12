package com.kyant.music

import android.app.Application
import com.kyant.music.storage.ResourceManager
import com.kyant.music.util.DataSaver

class HarmoniaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        DataSaver.init(filesDir.absolutePath)
        ResourceManager.init(this)
    }
}
