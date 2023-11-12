package com.kyant.media.android

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.kyant.media.R
import com.kyant.media.core.player.Player
import com.kyant.media.core.player.Player.Companion.TOGGLE_REPEAT_MODE
import com.kyant.media.core.player.Player.Companion.TOGGLE_SHUFFLE_MODE
import com.kyant.media.core.player.RepeatMode
import com.kyant.media.core.player.ShuffleMode

class MediaNotificationManager(
    private val context: Context,
    sessionActivity: Class<out Activity>,
    mediaSession: MediaSessionCompat
) {
    companion object {
        private const val CHANNEL_ID = "media_playback"
    }

    private val name = context.getString(R.string.media_playback_channel_name)
    private val descriptionText = context.getString(R.string.media_playback_channel_description)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val contentIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, sessionActivity),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val deleteIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(
        context,
        PlaybackStateCompat.ACTION_STOP
    )

    private val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
        .setMediaSession(mediaSession.sessionToken)
        .setShowActionsInCompactView(1, 2, 3)

    private val skipToPreviousAction = NotificationCompat.Action(
        R.drawable.skip_previous,
        "Previous",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )
    )
    private val playAction = NotificationCompat.Action(
        R.drawable.play,
        "Play",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_PLAY_PAUSE
        )
    )
    private val pauseAction = NotificationCompat.Action(
        R.drawable.pause,
        "Pause",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_PLAY_PAUSE
        )
    )
    private val skipToNextAction = NotificationCompat.Action(
        R.drawable.skip_next,
        "Next",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        )
    )
    private val repeatOffAction = NotificationCompat.Action(
        R.drawable.repeat_off,
        "Repeat Off",
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).apply {
                component = getMediaButtonReceiverComponent(context)
                action = TOGGLE_REPEAT_MODE
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )
    private val repeatOneAction = NotificationCompat.Action(
        R.drawable.repeat_once,
        "Repeat One",
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).apply {
                component = getMediaButtonReceiverComponent(context)
                action = TOGGLE_REPEAT_MODE
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )
    private val repeatAllAction = NotificationCompat.Action(
        R.drawable.repeat,
        "Repeat All",
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).apply {
                component = getMediaButtonReceiverComponent(context)
                action = TOGGLE_REPEAT_MODE
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )
    private val shuffleOffAction = NotificationCompat.Action(
        R.drawable.shuffle_disabled,
        "Shuffle Off",
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).apply {
                component = getMediaButtonReceiverComponent(context)
                action = TOGGLE_SHUFFLE_MODE
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )
    private val shuffleOnAction = NotificationCompat.Action(
        R.drawable.shuffle,
        "Shuffle On",
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).apply {
                component = getMediaButtonReceiverComponent(context)
                action = TOGGLE_SHUFFLE_MODE
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    )

    private fun getMediaButtonReceiverComponent(context: Context): ComponentName? {
        val queryIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        queryIntent.setPackage(context.packageName)
        val pm = context.packageManager
        val resolveInfos = pm.queryBroadcastReceivers(queryIntent, 0)
        if (resolveInfos.size == 1) {
            val resolveInfo = resolveInfos[0]
            return ComponentName(
                resolveInfo.activityInfo.packageName,
                resolveInfo.activityInfo.name
            )
        } else if (resolveInfos.size > 1) {
            Log.w(
                "MediaButtonReceiver",
                "More than one BroadcastReceiver that handles ${Intent.ACTION_MEDIA_BUTTON} was found, returning null."
            )
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun buildMediaNotification(player: Player): Notification? {
        if (player.playbackState == Player.PlaybackState.Idle) {
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSmallIcon(R.drawable.play_circle_outline)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                val mediaDescription = player.currentMediaItem?.mediaDescription
                setContentTitle(mediaDescription?.title ?: name)
                setContentText(mediaDescription?.subtitle ?: descriptionText)
                setLargeIcon(mediaDescription?.iconBitmap?.invoke())
            }
            setPriority(NotificationCompat.PRIORITY_MIN)
            setContentIntent(contentIntent)
            setDeleteIntent(deleteIntent)
            setStyle(mediaStyle)
            setOnlyAlertOnce(true)
            setShowWhen(false)
            val repeatAction = when (player.repeatMode) {
                RepeatMode.Off -> repeatOffAction
                RepeatMode.One -> repeatOneAction
                RepeatMode.All -> repeatAllAction
            }
            addAction(repeatAction)
            addAction(skipToPreviousAction)
            addAction(if (player.isPlaying) pauseAction else playAction)
            addAction(skipToNextAction)
            val shuffleAction = when (player.shuffleMode) {
                ShuffleMode.Off -> shuffleOffAction
                ShuffleMode.On -> shuffleOnAction
            }
            addAction(shuffleAction)
        }.build()
    }
}
