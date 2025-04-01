package com.example.compsoe0324player_service_prepare

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun NetworkImage(url: String) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var error by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        error = false
        loading = true
        withContext(Dispatchers.IO) {
            try {
                bitmap = URL(url).openStream().use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (e: Exception) {
                error = true
            } finally {
                loading = false
            }
        }
    }

    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .size(300.dp)
    ) {
        if (!loading) {
            bitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "")
            }
            if (error) {
                Text("error", modifier = Modifier.align(Alignment.Center))
            }
        } else {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}