package com.kyant.music.data.song

import kotlinx.serialization.Serializable

@Serializable
data class FileProperties(
    val path: String,
    val mimeType: String? = null,
    val size: Long = 0,
    val dateModified: Long = 0,
    val dateAdded: Long = 0
) {
    val fileName: String
        get() = path.substringAfterLast("/")

    val fileExtension: String
        get() = fileName.substringAfterLast(".")

    val folderName: String
        get() = path.substringBeforeLast("/")

    companion object {
        val Empty = FileProperties("")
    }
}
