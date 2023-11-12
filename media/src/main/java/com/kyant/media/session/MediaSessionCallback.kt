package com.kyant.media.session

import android.net.Uri
import android.os.Bundle
import com.kyant.media.core.item.MediaItem

/**
 * A callback for [MediaBrowserService] to handle media requests.
 */
interface MediaSessionCallback {

    fun onPlayFromMediaId(mediaId: String?, extras: Bundle?): MediaItem?

    fun onPlayFromUri(uri: Uri?, extras: Bundle?): MediaItem?

    fun getRecentMediaItemsAtDeviceBootTime(): List<MediaItem>?
}
