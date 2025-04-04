package com.example.compose_pre_0310

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.glance.appwidget.updateAll
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MediaPlayerService : Service() {
    companion object {
        private var instance: MediaPlayerService? = null
        fun getInstance(): MediaPlayerService? = instance

        private val _isPlaying = MutableStateFlow(false)
        val isPlaying: StateFlow<Boolean> = _isPlaying

        private val _hasPrepare = MutableStateFlow(false)
        val hasPrepare: StateFlow<Boolean> = _hasPrepare

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "media_player"
    }

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        initializePlayer()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun updateWidget() {
        Widget().updateAll(this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Media Playback",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Media player controls" }
        notificationManager.createNotificationChannel(channel)
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .build()

        mediaSession = MediaSession.Builder(this, player).build()
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                updateNotification()
                appScope.launch {
                    updateWidget()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _hasPrepare.value = playbackState == Player.STATE_READY
                appScope.launch {
                    updateWidget()
                }
            }
        })
    }

    @OptIn(UnstableApi::class)
    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent(this, MediaPlayerService::class.java).setAction("PLAY")
        val pauseIntent = Intent(this, MediaPlayerService::class.java).setAction("PAUSE")
        val stopIntent = Intent(this, MediaPlayerService::class.java).setAction("STOP")

        val playPendingIntent =
            PendingIntent.getService(this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val pausePendingIntent =
            PendingIntent.getService(this, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopPendingIntent =
            PendingIntent.getService(this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Media Player")
            .setContentText(if (_isPlaying.value) "Playing" else "Paused")
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                if (_isPlaying.value) R.drawable.pause else R.drawable.play,
                if (_isPlaying.value) "Pause" else "Play",
                if (_isPlaying.value) pausePendingIntent else playPendingIntent
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowActionsInCompactView(0)
            ).build()
    }

    private fun updateNotification() {
        notificationManager.notify(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> play()
            "PAUSE" -> pause()
            "STOP" -> stop()
        }
        return START_STICKY
    }

    fun init(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun toggle() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun stop() {
        player.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        instance = null
        super.onDestroy()
    }
}