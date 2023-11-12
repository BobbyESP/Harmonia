package com.kyant.media.core.item

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat

/**
 * A collection of multiple [song][PlayableItem]s or other [BrowsableItem][BrowsableItem]s which can be browsed,
 * such as an album and an artist (the songs in by the artist). It has a tree structure.
 */
interface BrowsableItem : MediaItem {

    fun getMediaItems(): List<MediaItem>

    fun flatten(parent: List<String>): List<Pair<List<String>, PlayableItem>> {
        return getMediaItems().flatMap {
            when (it) {
                is PlayableItem -> listOf(parent + it.mediaId to it)
                is BrowsableItem -> it.flatten(parent + it.mediaId)
            }
        }
    }

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
            MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        )
    }
}
