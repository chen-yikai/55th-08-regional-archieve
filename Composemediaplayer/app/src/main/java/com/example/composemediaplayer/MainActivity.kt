package com.example.composemediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.composemediaplayer.ui.theme.ComposemediaplayerTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposemediaplayerTheme {
                MediaPlayerScreen()
            }
        }
    }
}

@Composable
fun MediaPlayerScreen() {
    val mediaPlayer = MediaPlayer().apply {
        setDataSource("https://skills-music-api.eliaschen.dev/music/nature.mp3")
        prepare()
    }
    var isPlaying by remember { mutableStateOf(false) }
    LaunchedEffect(mediaPlayer.isPlaying) {
        isPlaying = mediaPlayer.isPlaying
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            } else {
                mediaPlayer.start()
                isPlaying = true
            }
        }, modifier = Modifier.size(200.dp)) {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}