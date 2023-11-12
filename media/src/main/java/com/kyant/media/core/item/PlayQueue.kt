package com.kyant.media.core.item

import android.support.v4.media.session.MediaSessionCompat.QueueItem
import com.kyant.media.core.player.Player

/**
 * The queue for the [Player] that can be directly played.
 * It contains [PlayableItem], [BrowsableItem] and [Playlist].
 */
class PlayQueue : MutableList<PieceOfMedia> by mutableListOf() {

    val playableItems by lazy {
        flatten()
    }

    private fun flatten(): List<Pair<List<String>, PlayableItem>> {
        return this.flatMap {
            when (it) {
                is PlayableItem -> listOf(listOf(it.mediaId) to it)
                is BrowsableItem -> it.flatten(emptyList())
                is Playlist -> it.flatten(emptyList())
            }
        }
    }

    fun asAndroidQueueItems(): List<QueueItem> {
        return playableItems.map { (path, mediaItem) ->
            QueueItem(mediaItem.asAndroidMediaItem().description, path.hashCode().toLong())
        }
    }

    fun getPlayableItemByQueueId(queueId: Long): Pair<Int, PlayableItem>? {
        return playableItems
            .mapIndexed { index, pair -> index to pair }
            .find { it.second.first.hashCode().toLong() == queueId }
            ?.let { it.first to it.second.second }
    }
}
