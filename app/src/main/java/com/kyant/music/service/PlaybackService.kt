package com.kyant.music.service

import android.net.Uri
import android.os.Bundle
import com.kyant.media.core.item.MediaItem
import com.kyant.media.session.MediaBrowserService
import com.kyant.media.session.MediaSessionCallback
import com.kyant.music.MainActivity
import com.kyant.music.storage.mediaStore

class PlaybackService : MediaBrowserService(
    sessionActivity = MainActivity::class.java,
    mediaItemTree = MediaItemTree,
    callback = object : MediaSessionCallback {
        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?): MediaItem? {
            return mediaId?.let { id -> mediaStore.getSong(id) }
        }

        override fun onPlayFromUri(uri: Uri?, extras: Bundle?): MediaItem? {
            return uri?.let { u -> mediaStore.getSong(u) }
        }

        override fun getRecentMediaItemsAtDeviceBootTime(): List<MediaItem> {
            return mediaStore.songs
        }
    }
)
