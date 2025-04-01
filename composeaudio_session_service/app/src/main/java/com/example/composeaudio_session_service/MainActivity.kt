package com.example.composeaudio_session_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composeaudio_session_service.ui.theme.Composeaudio_session_serviceTheme
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Composeaudio_session_serviceTheme {
                Column {
                    PlaybackScreen()
                    PlaybackScreen()
                }
            }
        }
    }
}

@Composable
fun PlaybackScreen(viewModel: PlaybackViewModel = viewModel()) {
    val context = LocalContext.current
    val controller by viewModel.controller.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentMediaItem by view Model.currentMediaItem.collectAsState()

    // Initialize the controller when the composable is first composed
    LaunchedEffect(Unit) {
        viewModel.initializeController(context)
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (controller != null) {
            Text(text = "Player Connected!")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Now Playing: ${currentMediaItem?.title ?: "None"}")

            Spacer(modifier = Modifier.height(16.dp))

            // Play/Pause button
            Button(onClick = {
                controller?.run {
                    if (isPlaying) pause() else play()
                }
            }) {
                Text(text = if (isPlaying) "Pause" else "Play")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stop button
            Button(onClick = {
                controller?.stop()
            }) {
                Text(text = "Stop")
            }
        } else {
            Text(text = "Connecting to Playback Service...")
        }
    }
}