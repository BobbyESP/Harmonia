package com.kyant.media.session

import com.kyant.media.core.item.BrowsableItem
import com.kyant.media.core.item.MediaItem

/**
 * A tree of [MediaItem]s for [MediaBrowserService] to browse.
 */
interface BaseMediaItemTree {

    /**
     * The root [BrowsableItem] of this tree.
     */
    val rootItem: BrowsableItem

    /**
     * Get a [MediaItem] by its [mediaId].
     */
    fun getMediaItem(mediaId: String): MediaItem?

    /**
     * Get the children of a [BrowsableItem] by its [parentId].
     */
    fun getChildren(parentId: String): List<MediaItem> {
        return (getMediaItem(parentId) as? BrowsableItem)?.getMediaItems() ?: emptyList()
    }
}
