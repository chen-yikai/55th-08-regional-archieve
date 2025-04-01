package com.example.compose_pre_0310

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.compose_pre_0310.ui.theme.Compose_pre_0310Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_pre_0310Theme {
                PlayerController("https://skills-music-api.eliaschen.dev/music/ocean.mp3")
//                sendNotify(this)
            }
        }
    }
}

@Composable
fun sendNotify(context: Context) {
    Box(Modifier.fillMaxSize()) {
        Button(onClick = {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            val notification =
                NotificationCompat.Builder(context, "kitty").setContentText("hello")
                    .setContentTitle("title").setSmallIcon(R.drawable.play).build()

            notificationManager.notify(323232, notification)
        }) {
            Text("Send")
        }
    }

}

@Composable
fun PlayerController(audioUrl: String) {
    val service = MediaPlayerService.getInstance()
    val isPlaying = MediaPlayerService.isPlaying.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                service?.init(audioUrl)
            }
        ) {
            Text("init")
        }
        Button(
            onClick = {
                if (isPlaying) {
                    service?.pause()
                } else {
                    service?.play()
                }
            }
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                service?.stop()
            }
        ) {
            Text("Stop")
        }
    }
}