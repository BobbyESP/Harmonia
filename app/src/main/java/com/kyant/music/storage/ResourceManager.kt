package com.kyant.music.storage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.core.net.toUri
import com.kyant.music.data.song.Song
import com.kyant.taglib.Metadata
import com.kyant.taglib.Picture
import com.kyant.taglib.TagLib
import java.io.File
import java.lang.ref.WeakReference

@Immutable
object ResourceManager {

    private lateinit var context: WeakReference<Context>

    private const val FRONT_COVER = "Front Cover"

    private val thumbnailsDirPath by lazy {
        File(context.get()!!.filesDir, "thumbnails").apply {
            mkdirs()
        }.absolutePath
    }

    fun init(context: Context) {
        ResourceManager.context = WeakReference(context)

        val uri = "content://${AppFileProvider.AUTHORITY}/thumbnails".toUri()
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
            Intent.FLAG_GRANT_PREFIX_URI_PERMISSION

        // TODO: Let this can be configured by user
        listOf(
            "com.android.systemui",
            "com.google.android.projection.gearhead",
            "com.example.android.mediacontroller"
        ).forEach { packageName ->
            context.grantUriPermission(packageName, uri, flags)
        }
    }

    fun getMetadata(song: Song): Metadata? {
        return try {
            useFd(song) {
                TagLib.getMetadata(it)
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun getPictures(song: Song): Array<Picture>? {
        return try {
            useFd(song) {
                TagLib.getPictures(it)
            }
        } catch (_: Exception) {
            null
        }
    }

    fun getFrontCover(song: Song): Bitmap? {
        return getCoverArt(song, FRONT_COVER)
    }

    private fun getCoverArt(song: Song, pictureType: String): Bitmap? {
        return try {
            val pictures = getPictures(song)
            val picture = pictures?.find { it.pictureType == pictureType }
            picture?.let { BitmapFactory.decodeByteArray(it.data, 0, it.data.size) }
        } catch (_: Exception) {
            null
        }
    }

    fun createThumbnail(song: Song) {
        try {
            val frontCover = getFrontCover(song)
            val thumbnail = frontCover?.scale(200, 200)
            frontCover?.recycle()

            File(thumbnailsDirPath).mkdirs()

            File(thumbnailsDirPath, "${song.mediaId}.jpg").apply {
                if (thumbnail == null) {
                    if (exists()) {
                        delete()
                    }
                    return
                }
                if (!exists()) {
                    createNewFile()
                }
                outputStream().use {
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 95, it)
                    thumbnail.recycle()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getThumbnailUri(song: Song): Uri? {
        val file = File(thumbnailsDirPath, "${song.mediaId}.jpg")
        if (!file.exists()) {
            return null
        }
        return FileProvider.getUriForFile(context.get()!!, AppFileProvider.AUTHORITY, file)
    }

    fun getLyrics(song: Song): String? {
        return try {
            useFd(song) {
                TagLib.getLyrics(it)
            }
        } catch (_: Exception) {
            null
        }
    }

    private inline fun <T> useFd(song: Song, block: (Int) -> T): T? {
        return context.get()!!.contentResolver.openFileDescriptor(song.uri, "r")?.use { pfd ->
            block(pfd.dup().detachFd())
        }
    }
}
