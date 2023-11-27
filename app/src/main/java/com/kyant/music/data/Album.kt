package com.kyant.music.data

import android.graphics.Bitmap
import android.net.Uri
import com.kyant.media.core.item.BrowsableItem
import com.kyant.media.core.item.MediaDescription
import com.kyant.music.data.song.Song
import com.kyant.music.storage.mediaStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class Album(
    private val name: String?,
    val albumArtist: ImmutableList<Artist>
) : BrowsableItem, SongList {

    override val mediaId: String
        get() = "$MEDIA_ID_PREFIX${hashCode()}"

    override fun getMediaItems(): List<Song> {
        return songs
    }

    override val mediaDescription: MediaDescription
        get() = MediaDescription(
            title = title,
            subtitle = displayAlbumArtist.takeUnless { isAlbumArtistUnknown() },
            iconUri = thumbnailUri,
            iconBitmap = { coverArt }
        )

    override val songs: ImmutableList<Song>
        get() = mediaStore.songSequence.filter { it.album == this }
            .sortedBy { it.metadata.trackNumber }
            .sortedBy { it.metadata.discNumber }
            .toImmutableList()

    val title: String
        get() = name ?: "<unknown>"

    val displayAlbumArtist: String
        get() = albumArtist.joinToString { it.title }

    val thumbnailUri: Uri?
        get() = mediaStore.songSequence.find { it.album == this }?.thumbnailUri

    val coverArt: Bitmap?
        get() = mediaStore.songSequence.find { it.album == this }?.coverArt

    fun isUnknown(): Boolean {
        return name == null
    }

    fun isAlbumArtistUnknown(): Boolean {
        return albumArtist.size == 1 && albumArtist.first().isUnknown()
    }

    companion object {
        const val MEDIA_ID_PREFIX = "album:"
    }
}
