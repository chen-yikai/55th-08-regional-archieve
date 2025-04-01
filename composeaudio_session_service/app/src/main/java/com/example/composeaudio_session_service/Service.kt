package com.example.composeaudio_session_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PlaybackViewModel : ViewModel() {
    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller: StateFlow<MediaController?> = _controller

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentMediaItem = MutableStateFlow<MediaMetadata?>(null)
    val currentMediaItem: StateFlow<MediaMetadata?> = _currentMediaItem

    @OptIn(UnstableApi::class)
    fun initializeController(context: Context) {
        viewModelScope.launch {
            val controllerFuture: ListenableFuture<MediaController> =
                PlaybackService.getController(context)
            controllerFuture.addListener(
                {
                    try {
                        val mediaController = controllerFuture.get()
                        _controller.value = mediaController
                        setupPlayerListeners(mediaController)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { it.run() }
            )
        }
    }

    // Set up listeners to update StateFlow based on player events
    private fun setupPlayerListeners(mediaController: MediaController) {
        mediaController.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onEvents(player: Player, events: Player.Events) {
                _currentMediaItem.value = player.mediaMetadata
                super.onEvents(player, events)
            }

            override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {

                super.onPlaylistMetadataChanged(mediaMetadata)
            }
        })
    }

    override fun onCleared() {
        _controller.value?.release()
        super.onCleared()
    }
}

@UnstableApi
class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null
    private lateinit var notificationManager: PlayerNotificationManager

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        val mediaItem =
            MediaItem.Builder()
                .setUri("https://skills-music-api-v2.eliaschen.dev/audio/ocean.mp3")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("Ocean")
                        .setDescription("wow this is so fucking good~")
                        .setArtworkUri(Uri.parse("https://skills-music-api-v2.eliaschen.dev/cover/ocean.jpg"))
                        .build()
                ).build()
        player!!.setMediaItem(mediaItem)
        mediaSession = MediaSession.Builder(this, player!!).build()
        setupNotification()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        notificationManager.setPlayer(null)
        super.onDestroy()
    }

    private fun setupNotification() {
        val notificationId = 1
        val channelId = "media_playback_channel"
        notificationManager = PlayerNotificationManager.Builder(this, notificationId, channelId)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: androidx.media3.common.Player) =
                    "Media Title"

                override fun createCurrentContentIntent(player: androidx.media3.common.Player) =
                    null

                override fun getCurrentContentText(player: androidx.media3.common.Player) =
                    "Media Artist"

                override fun getCurrentLargeIcon(
                    player:  Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ) = null
            })
            .build()
        notificationManager.setPlayer(player)
    }

    companion object {
        fun getController(context: Context): ListenableFuture<MediaController> {
            val serviceIntent = Intent(context, PlaybackService::class.java)
            context.startService(serviceIntent)
            val componentName = ComponentName(context, PlaybackService::class.java)
            val sessionToken = SessionToken(context, componentName)
            return MediaController.Builder(context, sessionToken).buildAsync()
        }
    }
}