package com.example.compose0324player_service_prepare_overself

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.compose0324player_service_prepare_overself.ui.theme.Compose0324player_service_prepare_overselfTheme
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose0324player_service_prepare_overselfTheme {
                PlayerScreen()
            }
        }
    }
}

@Composable
fun Home() {

}

@Composable
fun PlayerScreen() {
    val playerState by PlayerService.playerState.collectAsState()
    var data by remember { mutableStateOf<List<MusicData>>(emptyList()) }

    LaunchedEffect(Unit) {
        val client = OkHttpClient()
        val gson = Gson()
        try {
            Log.i("kitty", "Start fetching api")
            withContext(Dispatchers.IO) {
                val req = Request.Builder().url(host + "/sounds").build()
                data = client.newCall(req).execute().let {
                    gson.fromJson(it.body?.string(), object : TypeToken<List<MusicData>>() {}.type)
                }
            }
        } catch (e: Exception) {
            Log.i("kitty", "error")
            return@LaunchedEffect
        }
        val mediasList = mutableListOf<MediaItem>()
        data.forEachIndexed { index, item ->
            mediasList.add(
                MediaItem.Builder().setMediaId(index.toString()).setUri(host + item.audio)
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(item.name)
                            .setArtworkUri(Uri.parse(host + item.cover))
                            .setDescription(host + item.description).build()
                    ).build()
            )
        }
        Log.i("kitty", "media items")
        PlayerService.init(mediasList)
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text(playerState.metadata.title.toString())
            Slider(value = playerState.currentPosition.toFloat(), onValueChange = {
                PlayerService.playerInstance?.seekTo(it.toLong())
            }, valueRange = 0f..playerState.getDuration())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    playerState.formatCurrentPosition()
                )
            }
        }
    }
}