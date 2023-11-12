package com.kyant.media.core.item

/**
 * A piece of media (or only one playable item) that can be added to a [PlayQueue],
 * which can be a [MediaItem] or a [Playlist].
 */
sealed interface PieceOfMedia {
    val mediaId: String
}
