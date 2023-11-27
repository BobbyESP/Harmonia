package com.kyant.music.storage

import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kyant.music.data.Album
import com.kyant.music.data.Artist
import com.kyant.music.data.Genre
import com.kyant.music.data.song.Song
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

var mediaStore: MediaStore = FakeMediaStore

abstract class MediaStore {

    open val baseSongs: MutableList<Song> = mutableStateListOf()

    open val customFolders: MutableList<String> = mutableStateListOf()

    val songSequence: Sequence<Song> by derivedStateOf {
        baseSongs.asSequence()
    }

    val songs: ImmutableList<Song> by derivedStateOf {
        baseSongs.toImmutableList()
    }

    val albums: ImmutableList<Album> by derivedStateOf {
        songSequence.map { it.album }.distinct()
            .sortedBy { it.title }
            .toImmutableList()
    }

    val artists: ImmutableList<Artist> by derivedStateOf {
        songSequence.flatMap { it.artists }.distinct()
            .sortedBy { it.title }
            .toImmutableList()
    }

    val genres: ImmutableList<Genre> by derivedStateOf {
        songSequence.flatMap { it.genres }.distinct()
            .sortedBy { it.title }
            .toImmutableList()
    }

    val folders: ImmutableList<String> by derivedStateOf {
        songSequence.map { it.folder }.distinct()
            .sortedBy { it }
            .toImmutableList()
    }

    fun getSong(mediaId: String?): Song? {
        return songSequence.find { it.mediaId == mediaId }
    }

    fun getSong(mediaUri: Uri?): Song? {
        if (mediaUri == null) return null
        return songSequence.find { it.uri == mediaUri }
    }

    fun getAlbum(mediaId: String): Album? {
        return albums.find { it.mediaId == mediaId }
    }

    fun getArtist(mediaId: String): Artist? {
        return artists.find { it.mediaId == mediaId }
    }

    fun getGenre(mediaId: String): Genre? {
        return genres.find { it.mediaId == mediaId }
    }

    fun getFolder(mediaId: String): String? {
        return folders.find { it == mediaId }
    }

    fun querySongs(query: String): ImmutableList<Song> {
        return songSequence.filter {
            it.title.contains(query, true) ||
                it.displayArtist.contains(query, true) ||
                it.album.title.contains(query, true) ||
                it.album.displayAlbumArtist.contains(query, true)
        }.toImmutableList()
    }

    var isScanning by mutableStateOf(false)

    abstract suspend fun scan()
}
