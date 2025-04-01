package com.example.compose0320_liu

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose0320_liu.ui.theme.Compose0320_liuTheme
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
            Compose0320_liuTheme {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    var currentTime by remember { mutableStateOf("") }
    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }
    var offsetX = remember { Animatable(0f) }
    var fontSize = remember { Animatable(0f) }
    var showSetting by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }
    var size by remember { mutableStateOf(25f) }
    var speed by remember { mutableStateOf(1500f) }

    LaunchedEffect(Unit, speed) {
        while (isActive) {
            offsetX.animateTo(
                targetValue = -textW.toFloat(),
                animationSpec = tween(durationMillis = speed.toInt(), easing = LinearEasing)
            )
            offsetX.snapTo(boxW.toFloat())
        }
    }

    LaunchedEffect(Unit, size) {
        while (isActive) {
            fontSize.animateTo(
                targetValue = size - 20f,
                animationSpec = tween(durationMillis = speed.toInt(), easing = LinearEasing)
            )
            fontSize.snapTo(size)
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    if (showSetting) {
        ModalBottomSheet(onDismissRequest = { showSetting = false }) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("font Size", modifier = Modifier.width(70.dp))
                    Slider(value = size, onValueChange = { size = it }, valueRange = 25f..60f)
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Speed", modifier = Modifier.width(70.dp))
                    Slider(value = speed, onValueChange = { speed = it }, valueRange = 1000f..3000f)
                }
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .onGloballyPositioned {
                boxW = it.size.width
            }) {
        IconButton(
            onClick = {
                showSetting = true
            },
            modifier = Modifier.align(Alignment.TopStart)
        ) { Icon(Icons.Default.Settings, contentDescription = "") }
        Text(
            if (text.isNotEmpty()) text else currentTime,
            fontSize = fontSize.value.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .onGloballyPositioned {
                    textW = it.size.width
                }
                .offset(with(LocalDensity.current) {
                    offsetX.value.toDp()
                }, 0.dp)
        )
    }
}