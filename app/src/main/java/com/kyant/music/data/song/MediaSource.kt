package com.kyant.music.data.song

import android.content.ContentUris
import androidx.compose.runtime.Immutable
import com.kyant.music.util.SerializableUri
import kotlinx.serialization.Serializable

@Serializable
@Immutable
@JvmInline
value class MediaSource(val uri: SerializableUri) {

    val mediaStoreId: Long
        get() = ContentUris.parseId(uri)

    companion object {
        fun fromMediaStore(id: Long) = MediaSource(
            ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        )
    }
}
