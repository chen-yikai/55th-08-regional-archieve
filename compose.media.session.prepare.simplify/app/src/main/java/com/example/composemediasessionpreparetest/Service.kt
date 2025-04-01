package com.example.composemediasessionpreparetest

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.core.app.NotificationCompat
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    private var isForegroundStarted = false

    companion object {
        private val _isPlaying = MutableStateFlow(false)
        val isPlaying: StateFlow<Boolean> = _isPlaying
        var playerInstance: ExoPlayer? = null

        fun playAudio(items: List<MediaItem>) {
            playerInstance?.apply {
                setMediaItems(items)
                prepare()
                play()
            }
        }

        fun pauseAudio() {
            playerInstance?.pause()
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        playerInstance = player
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(state: Boolean) {
                _isPlaying.value = state
                super.onIsPlayingChanged(state)
            }
        })
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(object : MediaSession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: List<MediaItem>
                ): ListenableFuture<List<MediaItem>> {
                    return Futures.immediateFuture(mediaItems)
                }
            })
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!isForegroundStarted) {
            val notification = createNotification()
            startForeground(1, notification)
            isForegroundStarted = true
        }
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        playerInstance = null
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "media_channel")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Media Playback")
            .setContentText("Playing audio")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
}