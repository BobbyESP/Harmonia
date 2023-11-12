package com.kyant.music.data.song

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import com.kyant.media.core.item.MediaDescription
import com.kyant.media.core.item.PlayableItem
import com.kyant.music.data.Album
import com.kyant.music.data.Artist
import com.kyant.music.data.Genre
import com.kyant.music.storage.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val source: MediaSource,
    val fileProperties: FileProperties = FileProperties.Empty,
    val audioProperties: AudioProperties = AudioProperties.Empty,
    val metadata: AudioMetadata = AudioMetadata.Empty
) : PlayableItem {

    override val mediaId: String
        get() = "${MEDIA_ID_PREFIX}${source.uri.hashCode()}"

    override val mediaDescription: MediaDescription
        get() = MediaDescription(
            title = title,
            subtitle = displayArtist.takeUnless { isArtistUnknown() },
            iconUri = thumbnailUri,
            mediaUri = uri,
            extras = Bundle().apply {
                if (!album.isUnknown()) {
                    putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album.title)
                }
                if (!album.isAlbumArtistUnknown()) {
                    putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, album.displayAlbumArtist)
                }
                if (!isGenreUnknown()) {
                    putString(MediaMetadataCompat.METADATA_KEY_GENRE, displayGenre)
                }
                putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audioProperties.duration)
            },
            iconBitmap = { coverArt }
        )

    val uri: Uri
        get() = source.uri

    val thumbnailUri: Uri?
        get() = ResourceManager.getThumbnailUri(this)

    val coverArt: Bitmap?
        get() = ResourceManager.getFrontCover(this)

    val title: String
        get() = metadata.trackTitle
            ?: fileProperties.fileName

    val artists: ImmutableList<Artist>
        get() = if (metadata.trackArtist.isNotEmpty()) {
            metadata.trackArtist.map { Artist(it) }.toImmutableList()
        } else {
            persistentListOf(Artist.Unknown)
        }

    val displayArtist: String
        get() = artists.joinToString { it.title }

    val album: Album
        get() = Album(
            name = metadata.albumTitle,
            albumArtist = if (metadata.albumArtist.isNotEmpty()) {
                metadata.albumArtist.map { Artist(it) }.toImmutableList()
            } else {
                persistentListOf(Artist.Unknown)
            }
        )

    val genres: ImmutableList<Genre>
        get() = if (metadata.genre.isNotEmpty()) {
            metadata.genre.map { Genre(it) }.toImmutableList()
        } else {
            persistentListOf(Genre.Unknown)
        }

    val displayGenre: String
        get() = genres.joinToString { it.title }

    val folder: String
        get() = fileProperties.folderName

    val lyrics: String?
        get() = ResourceManager.getLyrics(this)

    fun isArtistUnknown(): Boolean {
        return artists.size == 1 && artists.first().isUnknown()
    }

    fun isGenreUnknown(): Boolean {
        return genres.size == 1 && genres.first().isUnknown()
    }

    companion object {
        const val MEDIA_ID_PREFIX = "song:"
    }
}
