package com.kyant.music.data.song

import com.kyant.music.util.SerializableImmutableList
import com.kyant.music.util.SerializableImmutableMap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AudioMetadata(
    val properties: SerializableImmutableMap<String, SerializableImmutableList<String>> = persistentMapOf()
) {
    val albumTitle: String?
        get() = properties["ALBUM"]?.firstOrNull()
    val trackTitle: String?
        get() = properties["TITLE"]?.firstOrNull()

    val albumArtist: ImmutableList<String>
        get() = properties["ALBUMARTIST"] ?: persistentListOf()
    val trackArtist: ImmutableList<String>
        get() = properties["ARTIST"] ?: persistentListOf()
    val composer: ImmutableList<String>
        get() = properties["COMPOSER"] ?: persistentListOf()
    val conductor: ImmutableList<String>
        get() = properties["CONDUCTOR"] ?: persistentListOf()

    val discNumber: Int?
        get() = run {
            val raw = properties["DISCNUMBER"]?.firstOrNull()
            raw?.toIntOrNull() ?: raw?.takeIf { it.contains("/") }?.substringBefore("/")?.toIntOrNull()
        }
    val discTotal: Int?
        get() = properties["DISCTOTAL"]?.firstOrNull()?.toIntOrNull() ?: run {
            val raw = properties["DISCNUMBER"]?.firstOrNull()
            raw?.takeIf { it.contains("/") }?.substringAfter("/")?.toIntOrNull()
        }
    val trackNumber: Int?
        get() = run {
            val raw = properties["TRACKNUMBER"]?.firstOrNull()
            raw?.toIntOrNull() ?: raw?.takeIf { it.contains("/") }?.substringBefore("/")?.toIntOrNull()
        }
    val trackTotal: Int?
        get() = properties["TRACKTOTAL"]?.firstOrNull()?.toIntOrNull() ?: run {
            val raw = properties["TRACKNUMBER"]?.firstOrNull()
            raw?.takeIf { it.contains("/") }?.substringAfter("/")?.toIntOrNull()
        }

    val year: Int
        get() = properties["DATE"]?.firstOrNull()?.toIntOrNull() ?: 0

    val isrc: String?
        get() = properties["ISRC"]?.firstOrNull()

    val compilation: Boolean
        get() = properties["COMPILATION"]?.firstOrNull()?.toIntOrNull() == 1

    val encodedBy: List<String>
        get() = properties["ENCODEDBY"]?.toList() ?: emptyList()

    val replayGainAlbumGain: String?
        get() = properties["REPLAYGAIN_ALBUM_GAIN"]?.firstOrNull()
    val replayGainAlbumPeak: String?
        get() = properties["REPLAYGAIN_ALBUM_PEAK"]?.firstOrNull()
    val replayGainTrackGain: String?
        get() = properties["REPLAYGAIN_TRACK_GAIN"]?.firstOrNull()
    val replayGainTrackPeak: String?
        get() = properties["REPLAYGAIN_TRACK_PEAK"]?.firstOrNull()

    val genre: ImmutableList<String>
        get() = properties["GENRE"] ?: persistentListOf()

    val comments: ImmutableList<String>
        get() = properties["COMMENT"] ?: persistentListOf()
    val copyrightMessage: String?
        get() = properties["COPYRIGHT"]?.firstOrNull()

    companion object {
        val Empty = AudioMetadata()
    }
}
