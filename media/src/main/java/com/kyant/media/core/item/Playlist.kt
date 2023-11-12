package com.kyant.media.core.item

/**
 * A playlist that can be played, which contains [PlayableItem] and [BrowsableItem].
 * It is a variant of [BrowsableItem].
 */
class Playlist(override val mediaId: String) : PieceOfMedia, MutableList<MediaItem> by mutableListOf() {

    fun flatten(parent: List<String>): List<Pair<List<String>, PlayableItem>> {
        return this.flatMap {
            when (it) {
                is PlayableItem -> listOf(parent + it.mediaId to it)
                is BrowsableItem -> it.flatten(parent + it.mediaId)
            }
        }
    }
}
