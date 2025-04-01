package com.example.compose0325test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

const val media_channel = "media_channel"
const val alarm_channel = "alarm_channel"

const val hostname = "https://skills-music-api-v2.eliaschen.dev"

enum class Screens {
    home, player
}

data class MusicApi(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val audio: String,
    val cover: String
)

fun fetchApi(): List<MusicApi> {
    val gson = Gson()
    val client = OkHttpClient()

    val req = Request.Builder().url("$hostname/sounds").build()
    val res = client.newCall(req).execute()
    return if (res.code == 200) res.let {
        gson.fromJson(
            it.body?.string(),
            object : TypeToken<List<MusicApi>>() {}.type
        )
    } else emptyList()
}

@Composable
fun NetworkImage(url: String, size: Dp, content: @Composable (Bitmap) -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                bitmap = URL(url).openStream().use {
                    BitmapFactory.decodeStream(it)
                }
                Log.i("kitty", "image success")
            } catch (e: Exception) {
                Log.i("kitty", "image error" + e)
            }
        }
    }
    bitmap?.let {
        content(it)
    }
    if (bitmap == null) {
        Box(Modifier.size(size)) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Sw(width: Dp = 20.dp) {
    Spacer(Modifier.width(width))
}

@Composable
fun Sh(height: Dp = 20.dp) {
    Spacer(
        Modifier.height(height)
    )
}

@Composable
fun CustomIconBtn(drawableRes: Int, size: Dp, click: () -> Unit) {
    IconButton(onClick = click, modifier = Modifier.size(size)) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = "", modifier = Modifier.fillMaxSize()
        )
    }
}