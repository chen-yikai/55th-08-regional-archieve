package com.example.compose0325test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class Widget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val playerState by MediaService.playerState.collectAsState()
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }
            var loading by remember { mutableStateOf(false) }

            LaunchedEffect(playerState.metadata.title, Unit) {
                loading = true
                withContext(Dispatchers.IO) {
                    try {
                        val bitmap_temp =
                            URL(playerState.metadata.artworkUri.toString()).openStream()
                                .use {
                                    BitmapFactory.decodeStream(it)
                                }
                        bitmap = bitmap_temp?.let {
                            Bitmap.createScaledBitmap(it, 200, 200, true).also { scaledBitmap ->
                                it.recycle()
                            }
                        }

                        Log.i(
                            "kitty",
                            "widget image success" + playerState.metadata.artworkUri.toString()
                        )
                    } catch (e: Exception) {
                        Log.i("kitty", "widget image error" + e)
                    } finally {
                        loading = false
                    }
                }
            }
            Column(
                GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!loading)
                    bitmap?.let {
                        Image(
                            provider = ImageProvider(it),
                            contentDescription = "",
                            GlanceModifier.size(130.dp)
                        )
                    }
                Spacer(GlanceModifier.height(10.dp))
                Text(
                    playerState.metadata.title.toString(),
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(GlanceModifier.height(20.dp))
                Row(
                    GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        imageProvider = ImageProvider(R.drawable.prev), contentDescription = "",
                        onClick = { MediaService.prev() }
                    )
                    Spacer(GlanceModifier.width(20.dp))
                    CircleIconButton(
                        imageProvider = ImageProvider(if (playerState.isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = "",
                        onClick = { MediaService.togglePlayPause() }
                    )
                    Spacer(GlanceModifier.width(20.dp))
                    CircleIconButton(
                        imageProvider = ImageProvider(R.drawable.next),
                        contentDescription = "",
                        onClick = { MediaService.next() }
                    )
                }
            }
        }
    }
}

class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Widget()
}