package com.kyant.media.session

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import androidx.media.MediaBrowserServiceCompat
import com.kyant.media.core.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A service that implements a media browser.
 */
open class MediaBrowserService(
    private val sessionActivity: Class<out Activity>,
    private val mediaItemTree: BaseMediaItemTree,
    private val callback: MediaSessionCallback
) : MediaBrowserServiceCompat() {

    companion object {
        private const val TAG = "MediaBrowserService"
        const val SERVICE_PLAYER_INTERFACE = "com.kyant.media.session.MediaBrowserService"
        private const val RECENT_LIBRARY_ROOT_MEDIA_ID = "com.kyant.media.session.recent.root"
        private const val RECENT_ITEM_MEDIA_ID = "com.kyant.media.session.recent.item"
        private const val SYSTEM_UI_PACKAGE_NAME = "com.android.systemui"
    }

    private val thread = HandlerThread(TAG).apply { start() }
    private val handler = Handler(thread.looper)

    private val scope = CoroutineScope(Dispatchers.IO)

    private val binder = LocalBinder()

    private lateinit var player: MediaSessionPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaSessionPlayer(this, handler, sessionActivity, callback)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        val isRecent = rootHints?.getBoolean(BrowserRoot.EXTRA_RECENT, false) ?: false
        if (clientPackageName == SYSTEM_UI_PACKAGE_NAME && isRecent) {
            if (!canResumePlaybackOnStart()) {
                return null
            }
            return BrowserRoot(RECENT_LIBRARY_ROOT_MEDIA_ID, rootHints)
        }
        return BrowserRoot(mediaItemTree.rootItem.mediaId, rootHints)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        if (parentId == RECENT_LIBRARY_ROOT_MEDIA_ID) {
            if (!canResumePlaybackOnStart()) {
                return result.sendResult(null)
            }
            result.detach()
            scope.launch {
                // Advertise support for playback resumption. If STATE_IDLE, the request arrives at boot time
                // to get the full item data to build a notification. If not STATE_IDLE we don't need to
                // deliver the full media item, so we do the minimal viable effort.
                return@launch if (player.playbackState == Player.PlaybackState.Idle) {
                    val recentItems = callback.getRecentMediaItemsAtDeviceBootTime()
                    result.sendResult(recentItems?.map { it.asAndroidMediaItem() }?.toMutableList())
                } else {
                    val recentItem = MediaItem(
                        MediaDescriptionCompat.Builder()
                            .setMediaId(RECENT_ITEM_MEDIA_ID)
                            .build(),
                        MediaItem.FLAG_PLAYABLE
                    )
                    result.sendResult(mutableListOf(recentItem))
                }
            }
        } else {
            result.detach()
            scope.launch {
                result.sendResult(mediaItemTree.getChildren(parentId).map { it.asAndroidMediaItem() }.toMutableList())
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.handleMediaButtonReceiverIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return when (intent?.action) {
            SERVICE_INTERFACE -> super.onBind(intent)
            SERVICE_PLAYER_INTERFACE -> binder
            else -> null
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (player.isPlaybackActive) return
        player.release()
        thread.quit()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        thread.quit()
    }

    private fun canResumePlaybackOnStart(): Boolean {
        return queryPackageManagerForMediaButtonReceiver() != null
    }

    // Needs to be provided in the app manifest.
    private fun queryPackageManagerForMediaButtonReceiver(): ComponentName? {
        val queryIntent = Intent(Intent.ACTION_MEDIA_BUTTON).apply {
            setPackage(packageName)
        }
        val resolveInfos = packageManager.queryBroadcastReceivers(queryIntent, 0)
        return if (resolveInfos.size == 1) {
            val resolveInfo = resolveInfos[0]
            ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
        } else if (resolveInfos.isEmpty()) {
            null
        } else {
            throw IllegalStateException(
                "Expected 1 broadcast receiver that handles ${Intent.ACTION_MEDIA_BUTTON}, found ${resolveInfos.size}"
            )
        }
    }

    inner class LocalBinder : Binder() {
        fun getPlayer(): Player = player
    }
}
