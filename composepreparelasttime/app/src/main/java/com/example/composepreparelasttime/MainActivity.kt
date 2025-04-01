package com.example.composepreparelasttime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composepreparelasttime.ui.theme.ComposepreparelasttimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposepreparelasttimeTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    var text by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("") }
    var offsetX = remember { Animatable(0f) }
    var currentTime by remember { mutableStateOf("") }
    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            offsetX.animateTo(
                targetValue = boxW.toFloat(),
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
            offsetX.snapTo(-textW.toFloat())
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .onGloballyPositioned {
                boxW = it.size.width
            }) {
        TextField(
            value = textInput,
            onValueChange = { textInput = it },
            modifier = Modifier
                .onKeyEvent { key ->
                    if (key.type == KeyEventType.KeyUp && key.key == Key.Enter) {
                        text = textInput
                        true
                    } else {
                        false
                    }
                }
                .fillMaxWidth(), singleLine = true
        )
        Text(
            if (text.isEmpty()) currentTime else text, modifier = Modifier
                .onGloballyPositioned {
                    textW = it.size.width
                }
                .align(Alignment.CenterStart)
                .offset(with(LocalDensity.current) { offsetX.value.toDp() }, 0.dp)
        )
    }
}