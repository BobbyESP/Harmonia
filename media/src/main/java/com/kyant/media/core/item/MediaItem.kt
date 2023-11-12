package com.kyant.media.core.item

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat

/**
 * The item that can be [played][PlayableItem] or [browsed][BrowsableItem].
 */
sealed interface MediaItem : PieceOfMedia {

    val mediaDescription: MediaDescription
        get() = MediaDescription()

    fun asAndroidMediaItem(): MediaItem

    fun asMediaMetadata(): MediaMetadataCompat = MediaMetadataCompat.Builder().apply {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
        mediaDescription.title?.let {
            putString(MediaMetadataCompat.METADATA_KEY_TITLE, it)
        }
        mediaDescription.subtitle?.let {
            putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it)
        }
        mediaDescription.extras?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)?.let {
            putString(MediaMetadataCompat.METADATA_KEY_ALBUM, it)
        }
        mediaDescription.extras?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)?.let {
            putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, it)
        }
        mediaDescription.extras?.getString(MediaMetadataCompat.METADATA_KEY_GENRE)?.let {
            putString(MediaMetadataCompat.METADATA_KEY_GENRE, it)
        }
        mediaDescription.extras?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.let {
            putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it)
        }
        putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, mediaDescription.iconBitmap())
    }.build()
}
