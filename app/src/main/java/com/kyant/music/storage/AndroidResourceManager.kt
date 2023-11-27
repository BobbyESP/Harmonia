package com.kyant.music.storage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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

@Immutable
class AndroidResourceManager(private val context: Context) : ResourceManager {

    private val thumbnailsDirPath by lazy {
        File(context.filesDir, "thumbnails").apply {
            mkdirs()
        }.absolutePath
    }

    init {
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

    override fun getMetadata(song: Song): Metadata? {
        return try {
            useFd(song) { fd, fileName ->
                TagLib.getMetadata(fd, fileName)
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun getPictures(song: Song): Array<Picture>? {
        return try {
            useFd(song) { fd, fileName ->
                TagLib.getPictures(fd, fileName)
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun createThumbnail(song: Song) {
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

    override fun getThumbnailUri(song: Song): Uri? {
        val file = File(thumbnailsDirPath, "${song.mediaId}.jpg")
        if (!file.exists()) {
            return null
        }
        return FileProvider.getUriForFile(context, AppFileProvider.AUTHORITY, file)
    }

    override fun getLyrics(song: Song): String? {
        return try {
            useFd(song) { fd, fileName ->
                TagLib.getLyrics(fd, fileName)
            }
        } catch (_: Exception) {
            null
        }
    }

    private inline fun <T> useFd(song: Song, block: (fd: Int, fileName: String) -> T): T? {
        return context.contentResolver.openFileDescriptor(song.uri, "r")?.use { pfd ->
            block(pfd.dup().detachFd(), song.fileProperties.fileName)
        }
    }
}
