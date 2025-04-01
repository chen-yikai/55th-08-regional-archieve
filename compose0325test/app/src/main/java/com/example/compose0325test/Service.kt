package com.example.compose0325test

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.glance.appwidget.updateAll
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PlayerState(
    var isPlaying: Boolean = false,
    var currentIndex: Int = 0,
    var metadata: MediaMetadata = MediaMetadata.Builder().build(),
    var duration: Long = 0,
    var currentPosition: Long = 0,
) {
    fun safeDuration(value: Long): Float = if (value > 0) value.toFloat() else 0f
    fun durationFormatter(value: Long): String {
        val seconds = value / 1000
        val mm = seconds / 60
        val ss = seconds % 60
        return if (seconds > 0) "${mm.toString().padStart(2, '0')}:${
            ss.toString().padStart(2, '0')
        }" else "00:00"
    }

    fun getDuration() = durationFormatter(duration)
    fun getCurrentPosition() = durationFormatter(currentPosition)
}

class MediaService : MediaSessionService() {
    var isRunning = false
    var mediaSession: MediaSession? = null
    lateinit var player: ExoPlayer
    var job: Job? = null
    var currentPositionJob: Job? = null

    companion object {
        var playerInstance: ExoPlayer? = null
        private var _playerState = MutableStateFlow(PlayerState())
        val playerState: StateFlow<PlayerState> = _playerState

        fun togglePlayPause() {
            playerInstance?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        }

        fun next() {
            playerInstance?.seekToNextMediaItem()
        }

        fun prev() {
            playerInstance?.seekToPreviousMediaItem()
        }
    }

    private fun initPlayer() {
        var api: List<MusicApi> = emptyList()
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            withContext(Dispatchers.IO) {
                api = fetchApi()
            }
        }
        if (api.isNotEmpty()) {
            player.clearMediaItems()
            api.forEachIndexed { index, item ->
                player.addMediaItem(
                    MediaItem.Builder().setMediaId(index.toString()).setUri(hostname + item.audio)
                        .setMediaMetadata(
                            MediaMetadata.Builder().setTitle(item.name)
                                .setDescription(item.description).setArtworkUri(
                                    Uri.parse(hostname + item.cover)
                                ).build()
                        )
                        .build()
                )
            }
            player.prepare()
            player.play()
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this@MediaService).build()
        playerInstance = player
        mediaSession = MediaSession.Builder(this@MediaService, player).build()
        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onEvents(player: Player, events: Player.Events) {
                _playerState.value = _playerState.value.copy(
                    isPlaying = player.isPlaying,
                    metadata = player.mediaMetadata,
                    duration = player.duration, currentIndex = player.currentMediaItemIndex
                )
                CoroutineScope(Dispatchers.Main).launch {
                    Widget().updateAll(this@MediaService)
                }
                val intent = Intent(this@MediaService, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this@MediaService,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                mediaSession?.setSessionActivity(pendingIntent)
                getCurrentPosition()
            }
        })
        startConnection()
    }

    private fun getCurrentPosition() {
        currentPositionJob?.cancel()
        currentPositionJob = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            while (player.isPlaying) {
                _playerState.value = _playerState.value.copy(
                    currentPosition = player.currentPosition
                )
                delay(500)
            }
        }
    }

    private fun startConnection() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            val sessionToken = SessionToken(
                this@MediaService,
                ComponentName(this@MediaService, MediaService::class.java)
            )
            MediaController.Builder(this@MediaService, sessionToken).buildAsync()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!isRunning) {
            val notification = NotificationCompat.Builder(this@MediaService, media_channel)
                .setContentTitle("Media Service").setContentText("Service Started").setOngoing(true)
                .setSmallIcon(R.drawable.app_icon).build()
            startForeground(1, notification)
            isRunning = true
        }
        return START_STICKY
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

}