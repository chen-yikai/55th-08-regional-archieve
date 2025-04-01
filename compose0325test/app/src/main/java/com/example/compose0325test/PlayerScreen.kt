package com.example.compose0325test

import android.net.Uri
import android.provider.MediaStore.Audio.Media
import android.transition.Slide
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun Player(nav: NavController) {
    var api by remember { mutableStateOf<List<MusicApi>>(emptyList()) }
    val playerState by MediaService.playerState.collectAsState()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            api = fetchApi()
        }
        if (api.isNotEmpty()) {
            MediaService.playerInstance?.apply {
                clearMediaItems()
                api.forEachIndexed { index, item ->
                    addMediaItem(
                        MediaItem.Builder().setMediaId(index.toString())
                            .setUri(hostname + item.audio)
                            .setMediaMetadata(
                                MediaMetadata.Builder().setTitle(item.name)
                                    .setDescription(item.description).setArtworkUri(
                                        Uri.parse(hostname + item.cover)
                                    ).build()
                            )
                            .build()
                    )
                }
                prepare()
                play()
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            CustomIconBtn(R.drawable.back, 20.dp) {
                nav.navigate(Screens.home.name)
            }
        }
        if (api.isNotEmpty()) {
            val current = api[playerState.currentIndex]
            Column(
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                key(playerState.currentIndex) {
                    NetworkImage(hostname + current.cover, 300.dp) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                }
                Sh()
                Text(current.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Slider(
                    value = playerState.safeDuration(playerState.currentPosition),
                    onValueChange = {
                        MediaService.playerInstance?.seekTo(it.toLong())
                    },
                    valueRange = 0f..playerState.safeDuration(playerState.duration)
                )
                Sh()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .offset(0.dp, (-10).dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(playerState.getCurrentPosition())
                    Text(playerState.getDuration())
                }
                Sh()
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomIconBtn(R.drawable.prev, 60.dp) {
                        MediaService.playerInstance?.seekToPreviousMediaItem()
                    }
                    CustomIconBtn(
                        if (playerState.isPlaying) R.drawable.pause else R.drawable.play,
                        70.dp
                    ) {
                        MediaService.togglePlayPause()
                    }
                    CustomIconBtn(R.drawable.next, 60.dp) {
                        MediaService.playerInstance?.seekToNextMediaItem()
                    }
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}