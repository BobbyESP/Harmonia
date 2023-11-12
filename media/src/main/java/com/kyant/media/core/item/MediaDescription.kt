package com.kyant.media.core.item

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat

/**
 * A description of media for building [MediaDescriptionCompat].
 */
class MediaDescription(
    val title: String? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val iconUri: Uri? = null,
    val extras: Bundle? = null,
    val mediaUri: Uri? = null,
    val iconBitmap: () -> Bitmap? = { null }
)
