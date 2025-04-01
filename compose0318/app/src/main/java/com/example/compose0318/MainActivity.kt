package com.example.compose0318

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.compose0318.ui.theme.Compose0318Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose0318Theme {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val context = LocalContext.current
    val sharePreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)

    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }

    var currentTime by remember { mutableStateOf("") }
    var offset = remember { Animatable(0f) }
    var showText by remember { mutableStateOf(true) }
    var showSheet by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf(sharePreferences.getString("text", "") ?: "") }
    var speed by remember { mutableStateOf(sharePreferences.getFloat("speed", 1500f)) }
    var color by remember { mutableStateOf(sharePreferences.getInt("color", 0)) }
    var blink by remember { mutableStateOf(sharePreferences.getBoolean("blink", true)) }
    var bgGradient by remember { mutableStateOf(sharePreferences.getBoolean("gradient", true)) }

    val colors = listOf(Color.Black, Color.Red, Color.Green, Color(0xffc9a5f5))

    // current time
    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime =
                SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000)
        }
    }

    // data store
    LaunchedEffect(text, speed, color, blink, bgGradient) {
        sharePreferences.edit().apply {
            putString("text", text)
            putFloat("speed", speed)
            putInt("color", color)
            putBoolean("blink", blink)
            putBoolean("gradient", bgGradient)
        }.apply()
    }

    // animation
    LaunchedEffect(Unit, speed) {
        while (isActive) {
            offset.animateTo(
                targetValue = boxW.toFloat(),
                animationSpec = tween(durationMillis = speed.toInt(), easing = LinearEasing)
            )
            offset.snapTo(-textW.toFloat())
        }
    }

    // blink
    LaunchedEffect(blink) {
        showText = true
        while (blink) {
            showText = !showText
            delay(500)
        }
    }

    // UI
    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = !showSheet }) {
            Column(Modifier.padding(horizontal = 15.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Speed", modifier = Modifier.width(70.dp))
                    Slider(value = speed, onValueChange = { speed = it }, valueRange = 500f..3000f)
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Blink")
                    Switch(checked = blink, onCheckedChange = { blink = it })
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bg Gradient")
                    Switch(checked = bgGradient, onCheckedChange = { bgGradient = it })
                }
                LazyRow {
                    itemsIndexed(colors) { index, item ->
                        Box(
                            Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(item)
                                .clickable {
                                    color = index
                                }
                        ) {
                            if (index == color) {
                                Icon(Icons.Default.Check, contentDescription = "")
                            }
                        }
                        Spacer(Modifier.width(15.dp))
                    }
                }
                Button(onClick = {
                    sharePreferences.edit().clear().apply()
                }, modifier = Modifier.fillMaxWidth()) { Text("Reset") }
            }
        }
    }

    Box(
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .onGloballyPositioned {
                boxW = it.size.width
            }
            .background(
                Brush.linearGradient(
                    if (bgGradient) listOf(
                        colors[color],
                        Color.White
                    ) else listOf(Color.White, Color.White)
                )
            )
    ) {
        IconButton(onClick = { showSheet = true }) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "",
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
        Text(
            if (text.isNotEmpty()) text else currentTime,
            color = colors[color],
            fontSize = 25.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(
                    with(
                        LocalDensity.current
                    ) {
                        offset.value.toDp()
                    }, 0.dp
                )
                .onGloballyPositioned {
                    textW = it.size.width
                }
        )
    }
}