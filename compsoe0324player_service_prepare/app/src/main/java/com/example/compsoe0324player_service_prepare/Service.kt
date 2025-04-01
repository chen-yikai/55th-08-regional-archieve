package com.example.compsoe0324player_service_prepare

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.LongState
import androidx.core.app.NotificationCompat
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val playback_channel = "mediaplayback_channel"

data class PlayerState(
    var isPlaying: Boolean = false,
    var currentIndex: Int = 0,
    var metadata: MediaMetadata = MediaMetadata.Builder().build(),
    var musicData: List<MusicSchema> = emptyList()
)

class PlayBackService : MediaSessionService() {
    private var hasStarted = false
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    private var controllerJob: Job? = null

    companion object {
        private val _playerState = MutableStateFlow(PlayerState())
        val playerState: StateFlow<PlayerState> = _playerState

        var playerInstance: ExoPlayer? = null

        fun init(items: List<MediaItem>) {
            playerInstance?.apply {
                setMediaItems(items)
                prepare()
                play()
            }
        }

        fun setMusicData(data: List<MusicSchema>) {
            _playerState.value = _playerState.value.copy(
                musicData = data
            )
        }

        fun seekToIndex(index: Int) {
            playerInstance?.apply {
                seekTo(index, 0)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this@PlayBackService).build()
        playerInstance = player
        var intent: Intent = Intent(this@PlayBackService, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingIntent)
            .build()
        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onEvents(player: Player, events: Player.Events) {
                _playerState.value = _playerState.value.copy(
                    isPlaying = player.isPlaying,
                    currentIndex = player.currentMediaItemIndex,
                    metadata = player.mediaMetadata
                )
                var intent: Intent = Intent(this@PlayBackService, MainActivity::class.java).apply {
                    putExtra("index", player.currentMediaItemIndex)
                }
                var pendingIntent = PendingIntent.getActivity(
                    this@PlayBackService,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                mediaSession!!.setSessionActivity(pendingIntent)
                super.onEvents(player, events)
            }
        })

        controllerJob?.cancel()
        controllerJob = CoroutineScope(Dispatchers.Main).launch {
            val sessionToken = SessionToken(
                this@PlayBackService,
                ComponentName(this@PlayBackService, PlayBackService::class.java)
            )
            MediaController.Builder(this@PlayBackService, sessionToken).buildAsync()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!hasStarted) {
            val notification =
                NotificationCompat.Builder(this, playback_channel).setSmallIcon(R.drawable.play)
                    .setContentTitle("Playback")
                    .setContentText("Ready to play")
                    .setOngoing(true)
                    .build()
            startForeground(1, notification)
            hasStarted = true
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.apply {
            player.release()
            release()
        }
        playerInstance = null
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

}