package com.example.compose0319_full

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.lifecycle.viewmodel.compose.viewModel

class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Column(GlanceModifier.fillMaxSize()) {
                Button(text = "Play", onClick = {
                    Player().toggle()
                })
            }
        }
    }

    @Composable
    fun MainView(player: Player = viewModel(), context: Context) {
        Column(
            GlanceModifier
                .fillMaxSize().background(Color.White)
        ) {
            Button(onClick = {
                player.initMediaApi(context = context)
            }, text = "click")
        }
    }
}

class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Widget()
}