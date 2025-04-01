package com.example.compsoe0324player_service_prepare

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun PlayerScreen(index: Int) {
    var musicData by remember { mutableStateOf<List<MusicSchema>>(emptyList()) }
    val playerState by PlayBackService.playerState.collectAsState()
    LaunchedEffect(Unit) {
        val client = OkHttpClient()
        val gson = Gson()

        try {
            withContext(Dispatchers.IO) {
                val request = Request.Builder().url("$host/sounds").build()
                musicData = client.newCall(request).execute().let { response ->
                    gson.fromJson(
                        response.body?.string(),
                        object : TypeToken<List<MusicSchema>>() {}.type
                    )
                }
            }
            PlayBackService.setMusicData(musicData)
            val mediaItems = musicData.mapIndexed { index, item ->
                MediaItem.Builder().setMediaId(index.toString()).setUri(host + item.audio)
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(item.name).setDescription(item.description)
                            .setArtworkUri(Uri.parse(host + item.cover)).build()
                    ).build()
            }
            PlayBackService.init(mediaItems)
        } catch (e: Exception) {
        } finally {
            PlayBackService.seekToIndex(index)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (musicData.isNotEmpty())
                key(playerState.currentIndex) {
                    NetworkImage(host + musicData.get(playerState.currentIndex).cover)
                }
            Spacer(Modifier.height(20.dp))
            Text(
                playerState.metadata.title.toString(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
        }
    }
}