package com.example.composemediasessionpreparetest

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            var player by remember { mutableStateOf<Player?>(null) }

            LaunchedEffect(Unit) {
                while (true) {
                    val sessionToken =
                        SessionToken(context, ComponentName(context, PlaybackService::class.java))
                    val controllerFuture =
                        MediaController.Builder(context, sessionToken).buildAsync()
                    controllerFuture.addListener(
                        {
                            player = controllerFuture.get()
                        },
                        Executors.newSingleThreadExecutor()
                    )
                    delay(500L)
                }
            }

            Column(
                GlanceModifier.fillMaxSize().background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(player?.mediaMetadata?.title.toString())
                Button(text = "Play/Pause", onClick = {
                    player?.pause()
                })
            }
        }
    }
}

class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Widget()
}