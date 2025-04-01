package com.example.compose0315

import android.content.Context
import android.os.Bundle
import android.os.strictmode.UntaggedSocketViolation
import android.view.RoundedCorner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.compose0315.ui.theme.Compose0315Theme
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
            Main()
        }
    }
}

enum class config {
    text, color, size, speed, direction, blink, gradient
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    var context = LocalContext.current
    var sharePreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    var openSetting by remember { mutableStateOf(false) }
    // config
    var text by remember { mutableStateOf(sharePreferences.getString(config.text.name, "") ?: "") }
    var color by remember { mutableStateOf(sharePreferences.getInt(config.color.name, 0)) }
    var size by remember { mutableStateOf(sharePreferences.getFloat(config.size.name, 12f)) }
    var speed by remember { mutableStateOf(sharePreferences.getFloat(config.speed.name, 1f)) }
    var direction by remember { mutableStateOf(sharePreferences.getInt(config.direction.name, 0)) }
    var blink by remember { mutableStateOf(sharePreferences.getBoolean(config.blink.name, true)) }
    var gradient by remember {
        mutableStateOf(
            sharePreferences.getBoolean(
                config.gradient.name,
                true
            )
        )
    }

    var currentTime by remember { mutableStateOf("") }
    var boxWidth by remember { mutableStateOf(0) }
    var boxHeight by remember { mutableStateOf(0) }
    var textWidth by remember { mutableStateOf(0) }
    var textHeight by remember { mutableStateOf(0) }
    var offsetX = remember { Animatable(0f) }
    var offsetY = remember { Animatable(0f) }
    var showText by remember { mutableStateOf(true) }
    var animatedSize = remember { Animatable(size) }

    // option
    val optColor = listOf(Color(0xff9273FF), Color(0xffFF8EB0))
    val optDirection = listOf("Horizontal", "Vertical")

    LaunchedEffect(direction) {
        sharePreferences.edit().apply {
            putString(config.text.name, text)
            putInt(config.color.name, color)
            putFloat(config.size.name, size)
            putFloat(config.speed.name, speed)
            putInt(config.direction.name, direction)
            putBoolean(config.blink.name, blink)
            putBoolean(config.gradient.name, gradient)
        }.apply()
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    LaunchedEffect(blink) {
        showText = true
        while (blink) {
            showText = !showText
            delay(500L)
        }
    }

    LaunchedEffect(size) {
        while (isActive) {
            animatedSize.animateTo(
                targetValue = size - 10,
                animationSpec = tween(
                    durationMillis = when (speed) {
                        0f -> 3000
                        1f -> 2000
                        3f -> 1000
                        else -> 1000
                    }, easing = LinearEasing
                )
            )
            animatedSize.snapTo(size)
        }
    }

    LaunchedEffect(
        speed, direction
    ) {
        while (direction == 0) {
            offsetX.animateTo(
                targetValue = -boxWidth.toFloat(),
                animationSpec = tween(
                    durationMillis = when (speed) {
                        0f -> 3000
                        1f -> 2000
                        3f -> 1000
                        else -> 1000
                    }, easing = LinearEasing
                )
            )
            offsetX.snapTo(textWidth.toFloat())
        }
    }

    LaunchedEffect(
        speed, direction
    ) {
        while (direction == 1) {
            offsetY.animateTo(
                targetValue = boxHeight.toFloat() + textHeight.toFloat(),
                animationSpec = tween(
                    durationMillis = when (speed) {
                        0f -> 3000
                        1f -> 2000
                        3f -> 1000
                        else -> 1000
                    }, easing = LinearEasing
                )
            )
            offsetY.snapTo(-textHeight.toFloat())
        }
    }

    if (openSetting) {
        ModalBottomSheet(onDismissRequest = { openSetting = false }) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                LazyRow {
                    itemsIndexed(optColor) { index, item ->
                        Box(modifier = Modifier
                            .padding(end = 10.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(optColor[index])
                            .clickable {
                                color = index
                            }

                        ) {
                            if (color == index) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "icon",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.BottomEnd)
                                        .padding(10.dp)
                                )
                            }
                        }

                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Font Size", modifier = Modifier.width(70.dp))
                    Slider(
                        value = size,
                        onValueChange = { size = it },
                        modifier = Modifier.fillMaxWidth(), valueRange = 12f..36f
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Speed", modifier = Modifier.width(70.dp))
                    Slider(
                        value = speed,
                        onValueChange = { speed = it },
                        valueRange = 0f..2f,
                        steps = 1
                    )
                }
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    optDirection.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = optDirection.size
                            ),
                            onClick = { direction = index },
                            selected = index == direction,
                            label = { Text(label) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Blink")
                    Switch(checked = blink, onCheckedChange = { blink = it })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gradient")
                    Switch(checked = gradient, onCheckedChange = { gradient = it })
                }
            }
        }
    }

    Box(
        Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .onGloballyPositioned {
                boxWidth = it.size.width
                boxHeight = it.size.height
            }) {
        IconButton(
            onClick = { openSetting = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        ) { Icon(Icons.Default.Settings, contentDescription = "") }
        if (showText) {
            Text(text = if (text.isEmpty()) currentTime else text,
                fontSize = animatedSize.value.sp,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        if (gradient) listOf(optColor[color], Color.White) else listOf(
                            Color.Black,
                            Color.Black

                        )
                    )
                ),
                modifier = Modifier
                    .zIndex(0f)
                    .align(if (direction == 0) Alignment.CenterEnd else Alignment.TopCenter)
                    .onGloballyPositioned {
                        textWidth = it.size.width
                        textHeight = it.size.height
                    }
                    .offset(if (direction == 0) with(LocalDensity.current) {
                        offsetX.value.toDp()
                    } else 0.dp, if (direction == 1) with(LocalDensity.current) {
                        offsetY.value.toDp()
                    } else 0.dp)
            )
        }
    }
}