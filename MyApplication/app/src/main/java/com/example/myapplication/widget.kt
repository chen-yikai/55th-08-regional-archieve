package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize

class BarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = BarWidget()
}

class BarWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Root()
        }
    }

    @Composable
    fun Root() {
        val context = LocalContext.current
        Column(GlanceModifier.fillMaxSize().background(Color.White).clickable {
            Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255)
        }) {
            Image(
                provider = ImageProvider(R.drawable.barcode),
                contentDescription = "bar code",
                modifier = GlanceModifier.fillMaxSize()
            )
        }
    }
}