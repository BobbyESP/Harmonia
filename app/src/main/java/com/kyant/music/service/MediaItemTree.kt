package com.kyant.music.service

import androidx.compose.runtime.Immutable
import com.kyant.media.core.item.BrowsableItem
import com.kyant.media.core.item.MediaDescription
import com.kyant.media.core.item.MediaItem
import com.kyant.media.session.BaseMediaItemTree
import com.kyant.music.data.Album
import com.kyant.music.data.Artist
import com.kyant.music.data.Genre
import com.kyant.music.data.song.Song
import com.kyant.music.storage.MediaStore

@Immutable
object MediaItemTree : BaseMediaItemTree {

    override val rootItem: BrowsableItem
        get() = root

    override fun getMediaItem(mediaId: String): MediaItem? = when {
        mediaId == rootItem.mediaId -> rootItem

        mediaId == songs.mediaId -> songs
        mediaId == albums.mediaId -> albums
        mediaId == artists.mediaId -> artists
        mediaId == genres.mediaId -> genres

        mediaId.startsWith(Song.MEDIA_ID_PREFIX) -> MediaStore.getSong(mediaId)
        mediaId.startsWith(Album.MEDIA_ID_PREFIX) -> MediaStore.getAlbum(mediaId)
        mediaId.startsWith(Artist.MEDIA_ID_PREFIX) -> MediaStore.getArtist(mediaId)
        mediaId.startsWith(Genre.MEDIA_ID_PREFIX) -> MediaStore.getGenre(mediaId)

        else -> null
    }

    private val root: BrowsableItem
        get() = object : BrowsableItem {
            override val mediaId: String
                get() = ":root"

            override fun getMediaItems(): List<BrowsableItem> {
                return categories
            }
        }

    private val songs = category(
        ":songs",
        "Songs",
        { "$it songs" },
        { MediaStore.songs }
    )
    private val albums = category(
        ":albums",
        "Albums",
        { "$it albums" },
        { MediaStore.albums }
    )
    private val artists = category(
        ":artists",
        "Artists",
        { "$it artists" },
        { MediaStore.artists }
    )
    private val genres = category(
        ":genres",
        "Genres",
        { "$it genres" },
        { MediaStore.genres }
    )

    private val categories = listOf(songs, albums, artists, genres)

    private inline fun category(
        mediaId: String,
        title: String,
        crossinline subtitle: (Int) -> String,
        crossinline getItems: () -> List<MediaItem>
    ) = object : BrowsableItem {
        override val mediaId: String
            get() = mediaId

        override val mediaDescription: MediaDescription
            get() = MediaDescription(
                title = title,
                subtitle = subtitle(getItems().size)
            )

        override fun getMediaItems(): List<MediaItem> = getItems()
    }
}
