package com.example.compose0320_scplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.compose0320_scplay.ui.theme.Compose0320_scplayTheme
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}


@Composable
fun Main() {
    var showSetting by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("這是跑馬燈") }
    var speed by remember { mutableStateOf(1500f) }
    var size by remember { mutableStateOf(20f) }
    var R by remember { mutableStateOf(0f) }
    var G by remember { mutableStateOf(0f) }
    var B by remember { mutableStateOf(0f) }

    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }
    var offsetX = remember { Animatable(0f) }

    LaunchedEffect(Unit, speed) {
        while (isActive) {
            offsetX.animateTo(
                targetValue = boxW.toFloat(),
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
            offsetX.snapTo(-textW.toFloat())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .onGloballyPositioned {
                boxW = it.size.width
            }
    ) {
        if (showSetting) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .zIndex(5f)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    IconButton(onClick = {
                        showSetting = false
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Column(Modifier.padding(horizontal = 20.dp)) {
                    SliderCustom(speed, "Speed", { speed = it }, 1000f..3000f)
                    SliderCustom(size, "Font Size", { size = it }, 20f..60f)
                    SliderCustom(R, "R", { R = it }, 0f..225f)
                    SliderCustom(G, "G", { G = it }, 0f..225f)
                    SliderCustom(B, "B", { B = it }, 0f..225f)
                }
            }
        }
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "") },
            trailingIcon = {
                IconButton(onClick = {
                    showSetting = !showSetting
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "")
                }
            })
        Text(
            text,
            fontSize = size.sp,
            color = Color(R, G, B),
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

@Composable
fun SliderCustom(
    value: Float,
    text: String,
    onChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text, modifier = Modifier.width(70.dp))
        Slider(value = value, onValueChange = onChange, valueRange = range)
    }
}