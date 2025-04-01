package com.example.compose0319_full

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(player: Player = viewModel()) {
    val context = LocalContext.current
    val isPlaying by player.isPlaying.collectAsState()
    val currentIndex by player.currentIndex.collectAsState()
    val mediaItem by player.mediaItem.collectAsState()
    val data by player.apiData.collectAsState()
    var slider by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        player.initMediaApi(context)
        delay(1000)
        player.initMediaApi(context)
    }

    if (data.isNotEmpty())
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 40.dp)
        ) {
            Column(
                Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NetworkImage(host + data[currentIndex].cover, currentIndex) {
                    Image(
                        bitmap = it, contentDescription = "", modifier = Modifier.clip(
                            RoundedCornerShape(20.dp)
                        )
                    )
                }
                Sh()
                Text(mediaItem.title.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Slider(value = slider, onValueChange = {
                    slider = it
                    player.player.seekTo(5000)
                }, valueRange = 0f..200f)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        player.prev()
                    }, modifier = Modifier.size(50.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.round_skip_previous_24),
                            contentDescription = "", modifier = Modifier.fillMaxSize()
                        )
                    }
                    IconButton(onClick = {
                        player.toggle()
                    }, modifier = Modifier.size(70.dp)) {
                        Icon(
                            painter = painterResource(if (isPlaying) R.drawable.round_pause_24 else R.drawable.round_play_arrow_24),
                            contentDescription = "", modifier = Modifier.fillMaxSize()
                        )
                    }
                    IconButton(onClick = {
                        player.next()
                    }, modifier = Modifier.size(50.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.round_skip_next_24),
                            contentDescription = "", modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
}
