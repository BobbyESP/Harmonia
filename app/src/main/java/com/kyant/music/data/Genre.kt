package com.kyant.music.data

import com.kyant.media.core.item.BrowsableItem
import com.kyant.media.core.item.MediaDescription
import com.kyant.music.data.song.Song
import com.kyant.music.storage.MediaStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Genre(private val name: String?) : BrowsableItem, SongList {

    override val mediaId: String
        get() = "$MEDIA_ID_PREFIX${hashCode()}"

    override fun getMediaItems(): List<Song> {
        return songs
    }

    override val mediaDescription: MediaDescription
        get() = MediaDescription(title = title)

    override val songs: ImmutableList<Song>
        get() = MediaStore.songSequence.filter { it.genres.contains(this) }
            .sortedBy { it.metadata.trackNumber }
            .sortedBy { it.metadata.discNumber }
            .sortedBy { it.album.title }
            .toImmutableList()

    val title: String
        get() = name ?: "<unknown>"

    fun isUnknown(): Boolean {
        return name == null
    }

    companion object {
        const val MEDIA_ID_PREFIX = "genre:"
        val Unknown = Genre(null)
    }
}
