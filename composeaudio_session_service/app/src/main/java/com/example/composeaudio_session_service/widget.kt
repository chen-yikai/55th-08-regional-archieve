package com.example.composeaudio_session_service

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            PlaybackScreen(context = context)
        }
    }
    @Composable
    fun PlaybackScreen(context: Context) {
        var viewMode = PlaybackViewModel()
        val controller by viewMode.controller.collectAsState()
        val isPlaying by viewMode.isPlaying.collectAsState()
        val currentMediaItem by viewMode.currentMediaItem.collectAsState()


        LaunchedEffect(Unit) {
            viewMode.initializeController(context)
        }

        Text("hello from glance ${currentMediaItem?.title}")
    }
}

class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Widget()
}