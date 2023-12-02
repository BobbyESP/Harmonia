package com.kyant.music.storage

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.bumptech.glide.Glide
import com.kyant.music.data.song.AudioMetadata
import com.kyant.music.data.song.AudioProperties
import com.kyant.music.data.song.FileProperties
import com.kyant.music.data.song.MediaSource
import com.kyant.music.data.song.Song
import com.kyant.music.util.mutableSaveableListStateOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureNanoTime
import kotlin.streams.toList as kotlinToList

class AndroidMediaStore(private val context: Context) : com.kyant.music.storage.MediaStore() {

    override val baseSongs = mutableSaveableListStateOf<Song>("songs.lst")

    override val customFolders = mutableSaveableListStateOf<String>("custom_folders")

    override suspend fun scan() {
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
                val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED).takeIf { it != -1 }
                val dateAddedColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED).takeIf { it != -1 }

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
                    val tag = resourceManager.getMetadata(song)

                    resourceManager.createThumbnail(song)

                    song.copy(
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
