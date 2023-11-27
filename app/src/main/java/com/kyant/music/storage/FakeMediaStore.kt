package com.kyant.music.storage

import com.kyant.music.data.song.AudioMetadata
import com.kyant.music.data.song.AudioProperties
import com.kyant.music.data.song.FileProperties
import com.kyant.music.data.song.MediaSource
import com.kyant.music.data.song.Song
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

object FakeMediaStore : MediaStore() {
    override suspend fun scan() {
        isScanning = true
        baseSongs.clear()
        baseSongs += listOf(
            Song(
                source = MediaSource.fromMediaStore(1),
                fileProperties = FileProperties(
                    path = "/storage/emulated/0/Music/01.mp3",
                    mimeType = "audio/mpeg",
                    size = 10 * 1024 * 1024,
                    dateModified = 0,
                    dateAdded = 0
                ),
                audioProperties = AudioProperties(
                    duration = 3 * 60 * 1000,
                    bitrate = 320 * 1024,
                    sampleRate = 44100,
                    channels = 2
                ),
                metadata = AudioMetadata(
                    persistentMapOf(
                        "TITLE" to persistentListOf("Song 01"),
                        "ALBUM" to persistentListOf("Album 1"),
                        "ARTIST" to persistentListOf("Artist 1")
                    )
                )
            ),
            Song(
                source = MediaSource.fromMediaStore(2),
                fileProperties = FileProperties(
                    path = "/storage/emulated/0/Music/02.mp3",
                    mimeType = "audio/mpeg",
                    size = 10 * 1024 * 1024,
                    dateModified = 0,
                    dateAdded = 0
                ),
                audioProperties = AudioProperties(
                    duration = 3 * 60 * 1000,
                    bitrate = 320 * 1024,
                    sampleRate = 44100,
                    channels = 2
                ),
                metadata = AudioMetadata(
                    persistentMapOf(
                        "TITLE" to persistentListOf("Song 02"),
                        "ALBUM" to persistentListOf("Album 1"),
                        "ARTIST" to persistentListOf("Artist 1")
                    )
                )
            ),
            Song(
                source = MediaSource.fromMediaStore(3),
                fileProperties = FileProperties(
                    path = "/storage/emulated/0/Music/03.mp3",
                    mimeType = "audio/mpeg",
                    size = 10 * 1024 * 1024,
                    dateModified = 0,
                    dateAdded = 0
                ),
                audioProperties = AudioProperties(
                    duration = 3 * 60 * 1000,
                    bitrate = 320 * 1024,
                    sampleRate = 44100,
                    channels = 2
                ),
                metadata = AudioMetadata(
                    persistentMapOf(
                        "TITLE" to persistentListOf("Song 03"),
                        "ALBUM" to persistentListOf("Album 2"),
                        "ARTIST" to persistentListOf("Artist 2", "Artist 3")
                    )
                )
            )
        )

        isScanning = false
    }
}
