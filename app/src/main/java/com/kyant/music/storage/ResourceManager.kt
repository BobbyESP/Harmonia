package com.kyant.music.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.kyant.music.data.song.Song
import com.kyant.taglib.Metadata
import com.kyant.taglib.Picture

var resourceManager: ResourceManager = FakeResourceManager

interface ResourceManager {

    companion object {
        private const val FRONT_COVER = "Front Cover"
    }

    fun getMetadata(song: Song): Metadata?

    fun getPictures(song: Song): Array<Picture>?

    fun createThumbnail(song: Song)

    fun getThumbnailUri(song: Song): Uri?

    fun getLyrics(song: Song): String?

    fun getFrontCover(song: Song): Bitmap? {
        return try {
            val pictures = getPictures(song)
            val picture = pictures?.find { it.pictureType == FRONT_COVER } ?: pictures?.firstOrNull()
            picture?.let { BitmapFactory.decodeByteArray(it.data, 0, it.data.size) }
        } catch (_: Exception) {
            null
        }
    }

    fun getCoverArt(song: Song, pictureType: String): Bitmap? {
        return try {
            val pictures = getPictures(song)
            val picture = pictures?.find { it.pictureType == pictureType }
            picture?.let { BitmapFactory.decodeByteArray(it.data, 0, it.data.size) }
        } catch (_: Exception) {
            null
        }
    }
}
