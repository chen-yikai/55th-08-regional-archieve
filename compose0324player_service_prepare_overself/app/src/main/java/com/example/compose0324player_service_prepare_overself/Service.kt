package com.example.compose0324player_service_prepare_overself

import android.content.ComponentName
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
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

const val player_channel = "player_channel"
const val host = "https://skills-music-api-v2.eliaschen.dev"

data class MusicData(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val audio: String,
    val cover: String
)

data class PlayerState(
    val isPlaying: Boolean = false,
    val duration: Long = 0,
    val currentPosition: Long = 0,
    val metadata: MediaMetadata = MediaMetadata.Builder().build()
) {
    fun getDuration(): Float = if (duration > 0) duration.toFloat() else 0f

    fun formatCurrentPosition(): String {
        val seconds = currentPosition / 1000
        val mm = seconds / 60
        val ss = seconds % 60

        return if (seconds > 0) "${mm.toString().padStart(2, '0')}:${
            ss.toString().padStart(2, '0')
        }" else "00:00"
    }

    fun timeFormater(float: Long): String {
        val seconds = float / 1000
        val mm = seconds / 60
        val ss = seconds % 60
        return "${mm.toString().padStart(2, '0')}:${ss.toString().padStart(2, '0')}"
    }
}

class PlayerService : MediaSessionService() {
    var hasStarted = false
    var mediaSession: MediaSession? = null
    lateinit var player: ExoPlayer
    var job: Job? = null
    var positionJob: Job? = null

    companion object {
        var _playerState = MutableStateFlow(PlayerState())
        val playerState: StateFlow<PlayerState> = _playerState
        var playerInstance: ExoPlayer? = null

        fun seekToPosition(target: Float) {
            playerInstance?.seekTo(target.toLong())
        }

        fun init(items: List<MediaItem>) {
            playerInstance?.apply {
                setMediaItems(items)
                prepare()
                play()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this@PlayerService).build()
        playerInstance = player
        player.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                _playerState.value = _playerState.value.copy(
                    metadata = player.mediaMetadata,
                    isPlaying = player.isPlaying,
                    duration = player.duration,
                )
                currentPosition()
            }
        })
        mediaSession = MediaSession.Builder(this@PlayerService, player).build()
        makeConnection()
    }

    fun currentPosition() {
        positionJob?.cancel()
        positionJob = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            while (player.isPlaying) {
                _playerState.value = _playerState.value.copy(
                    currentPosition = player.currentPosition
                )
                delay(500)
            }
        }
    }

    private fun makeConnection() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            val sessionToken = SessionToken(
                this@PlayerService,
                ComponentName(this@PlayerService, PlayerService::class.java)
            )
            MediaController.Builder(this@PlayerService, sessionToken).buildAsync()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!hasStarted) {
            val notification = NotificationCompat.Builder(this@PlayerService, player_channel)
                .setContentTitle("Player").setContentText("Ready to play")
                .setSmallIcon(R.drawable.ic_launcher_foreground).setOngoing(true).build()
            startForeground(1, notification)
            hasStarted = true
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.run {
            player.release()
            release()
        }
        playerInstance = null
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}