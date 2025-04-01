package com.example.composeban_lastday_prepare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeban_lastday_prepare.ui.theme.Composeban_lastday_prepareTheme
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
            Composeban_lastday_prepareTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    var text by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }
    var currentText by remember { mutableStateOf("") }
    var offsetX = remember { Animatable(0f) }
    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (isActive) {
            currentText =
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
            .onGloballyPositioned { boxW = it.size.width }) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent { k ->
                    if (k.type == KeyEventType.KeyUp && k.key == Key.Enter) {
                        text = inputText
                        true
                    } else {
                        false
                    }
                }, singleLine = true,
            placeholder = { Text("Place Holder") }
        )

        Text(
            if (text.isNotEmpty()) text else currentText,
            modifier = Modifier
                .onGloballyPositioned {
                    textW = it.size.width
                }
                .offset(with(LocalDensity.current) {
                    offsetX.value.toDp()
                }, 0.dp)
                .align(Alignment.CenterStart)
        )
    }
}