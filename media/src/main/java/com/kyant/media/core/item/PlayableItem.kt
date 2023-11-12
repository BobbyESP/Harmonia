package com.kyant.media.core.item

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat

/**
 * The item that can be played, usually representing a song.
 */
interface PlayableItem : MediaItem {

    override fun asAndroidMediaItem(): MediaBrowserCompat.MediaItem {
        return MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setTitle(mediaDescription.title)
                .setSubtitle(mediaDescription.subtitle)
                .setDescription(mediaDescription.description)
                .setIconUri(mediaDescription.iconUri)
                .setExtras(mediaDescription.extras)
                .setMediaUri(mediaDescription.mediaUri)
                .build(),
            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        )
    }
}
