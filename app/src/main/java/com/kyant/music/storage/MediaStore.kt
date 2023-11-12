package com.kyant.music.storage

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.bumptech.glide.Glide
import com.kyant.music.data.Album
import com.kyant.music.data.Artist
import com.kyant.music.data.Genre
import com.kyant.music.data.song.AudioMetadata
import com.kyant.music.data.song.AudioProperties
import com.kyant.music.data.song.FileProperties
import com.kyant.music.data.song.MediaSource
import com.kyant.music.data.song.Song
import com.kyant.music.util.mutableSaveableListStateOf
import kotlin.streams.toList as kotlinToList
import kotlin.system.measureNanoTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStore {

    private val baseSongs = mutableSaveableListStateOf<Song>("songs")

    private val customFolders = mutableSaveableListStateOf<Song>("custom_folders")

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
        private set

    suspend fun scan(context: Context) {
        withContext(Dispatchers.Main) {
            isScanning = true
        }

        measureNanoTime {
            withContext(Dispatchers.Main) {
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf("/storage/emulated/0"),
                    null
                ) { _, _ -> }
            }

            val songs = mutableListOf<Song>()

            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                null,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val mimeTypeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE).takeIf { it != -1 }
                val sizeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE).takeIf { it != -1 }
                val dateModifiedColumn =
                    cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED).takeIf { it != -1 }
                val dateAddedColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED).takeIf { it != -1 }

                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION).takeIf { it != -1 }
                val bitrateColumn = cursor.getColumnIndex(MediaStore.Audio.Media.BITRATE).takeIf { it != -1 }

                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE).takeIf { it != -1 }
                val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM).takeIf { it != -1 }
                val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST).takeIf { it != -1 }
                val albumArtistColumn =
                    cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST).takeIf { it != -1 }
                val discNumberColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISC_NUMBER).takeIf { it != -1 }
                val trackNumberColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK).takeIf { it != -1 }
                val yearColumn = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR).takeIf { it != -1 }
                val genreColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cursor.getColumnIndex(MediaStore.Audio.Media.GENRE).takeIf { it != -1 }
                } else {
                    null
                }

                while (cursor.moveToNext()) {
                    val path = cursor.getStringOrNull(pathColumn) ?: continue
                    songs += Song(
                        source = MediaSource.fromMediaStore(id = cursor.getLong(idColumn)),
                        fileProperties = FileProperties(
                            path = path,
                            mimeType = mimeTypeColumn?.let { cursor.getStringOrNull(it) },
                            size = sizeColumn?.let { cursor.getLongOrNull(it) } ?: 0,
                            dateModified = dateModifiedColumn?.let { cursor.getLongOrNull(it) } ?: 0,
                            dateAdded = dateAddedColumn?.let { cursor.getLongOrNull(it) } ?: 0
                        ),
                        audioProperties = AudioProperties(
                            duration = durationColumn?.let { cursor.getLongOrNull(it) } ?: 0,
                            bitrate = bitrateColumn?.let { cursor.getIntOrNull(it)?.div(1024) } ?: 0
                        ),
                        metadata = AudioMetadata(
                            properties = mutableMapOf<String, ImmutableList<String>>().apply {
                                titleColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("TITLE", persistentListOf(it))
                                }
                                albumColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("ALBUM", persistentListOf(it))
                                }
                                artistColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("ARTIST", persistentListOf(it))
                                }
                                albumArtistColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("ALBUMARTIST", persistentListOf(it))
                                }
                                discNumberColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("DISCNUMBER", persistentListOf(it))
                                }
                                trackNumberColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("TRACKNUMBER", persistentListOf(it))
                                }
                                yearColumn?.let { cursor.getIntOrNull(it) }?.let {
                                    put("DATE", persistentListOf(it.toString()))
                                }
                                genreColumn?.let { cursor.getStringOrNull(it) }?.let {
                                    put("GENRE", persistentListOf(it))
                                }
                            }.toImmutableMap()
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    baseSongs.replaceAll(songs)
                    Toast.makeText(
                        context,
                        "Basically scanned ${songs.size} songs, please wait for the full scan.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Glide.get(context).clearDiskCache()

                val newSongs = songs.parallelStream().map { song ->
                    val tag = ResourceManager.getMetadata(song)

                    ResourceManager.createThumbnail(song)

                    Song(
                        source = song.source,
                        fileProperties = song.fileProperties,
                        audioProperties = AudioProperties(
                            duration = tag?.audioProperties?.length?.toLong() ?: 0,
                            bitrate = tag?.audioProperties?.bitrate ?: 0,
                            sampleRate = tag?.audioProperties?.sampleRate ?: 0,
                            channels = tag?.audioProperties?.channels ?: 0
                        ),
                        metadata = AudioMetadata(
                            properties = tag?.propertyMap?.mapValues {
                                it.value.toList().toImmutableList()
                            }.orEmpty().toImmutableMap()
                        )
                    )
                }.run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        toList()
                    } else {
                        kotlinToList()
                    }
                }

                withContext(Dispatchers.Main) {
                    baseSongs.replaceAll(newSongs)
                    isScanning = false
                }
            }
        }.also { time ->
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Fully scan ended, totally in ${time / 1_000_000} ms.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
