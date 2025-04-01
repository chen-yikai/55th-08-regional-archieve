package com.example.compose0319_full

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

const val host = "https://skills-music-api-v2.eliaschen.dev"

data class Music(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val audio: String,
    val cover: String
)


@Composable
fun NetworkImage(url: String, chagne: Int, content: @Composable (ImageBitmap) -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit, chagne) {
        withContext(Dispatchers.IO) {
            bitmap = URL(url).openStream().use {
                BitmapFactory.decodeStream(it)
            }
        }
    }
    bitmap?.let {
        content(it.asImageBitmap())
    }
}

@Composable
fun Sh(height: Dp = 20.dp) {
    Spacer(Modifier.height(height))
}