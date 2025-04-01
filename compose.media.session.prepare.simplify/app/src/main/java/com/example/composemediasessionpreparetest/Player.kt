package com.example.myapp

import android.content.ComponentName
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composemediasessionpreparetest.PlaybackService
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Composable
fun MediaPlayerScreen() {
    val isPlaying by PlaybackService.isPlaying.collectAsState()
    val audioItems = listOf(
        MediaItem.Builder().setUri("https://skills-music-api-v2.eliaschen.dev/audio/ocean.mp3")
            .setMediaMetadata(
                MediaMetadata.Builder().setTitle("Ocean")
                    .setArtworkUri(Uri.parse("https://skills-music-api-v2.eliaschen.dev/cover/ocean.jpg"))
                    .build()
            ).build(),
        MediaItem.Builder().setUri("https://skills-music-api-v2.eliaschen.dev/audio/city.mp3")
            .setMediaMetadata(
                MediaMetadata.Builder().setTitle("City")
                    .setArtworkUri(Uri.parse("https://skills-music-api-v2.eliaschen.dev/cover/city.jpg"))
                    .build()
            ).build(),
    )

    Column(Modifier.statusBarsPadding()) {
        Text(if (isPlaying) "t" else "f")
        Text(PlaybackService.playerInstance?.mediaMetadata?.title.toString())
        Button(
            onClick = {
                PlaybackService.playAudio(audioItems)
            },
        ) {
            Text("Play Ocean Audio")
        }
        Button(
            onClick = { PlaybackService.playerInstance?.pause() },
        ) {
            Text("Pause")
        }
    }
}