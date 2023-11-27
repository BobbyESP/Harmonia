package com.kyant.music.storage

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.kyant.music.data.song.Song
import com.kyant.taglib.Metadata
import com.kyant.taglib.Picture

@Immutable
object FakeResourceManager : ResourceManager {

    override fun getMetadata(song: Song): Metadata? = null

    override fun getPictures(song: Song): Array<Picture>? = null

    override fun createThumbnail(song: Song) = Unit

    override fun getThumbnailUri(song: Song): Uri? = null

    override fun getLyrics(song: Song): String? {
        return getMetadata(song)?.propertyMap?.get("LYRICS")?.firstOrNull()
    }
}
