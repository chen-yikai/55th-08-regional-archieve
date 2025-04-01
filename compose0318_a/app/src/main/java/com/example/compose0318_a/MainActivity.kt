package com.example.compose0318_a

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose0318_a.ui.theme.Compose0318_aTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose0318_aTheme {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    var context = LocalContext.current
    var sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    val scope = rememberCoroutineScope()

    var sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var boxW by remember { mutableStateOf(0) }
    var textW by remember { mutableStateOf(0) }
    var open by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var textList = remember { mutableStateListOf<String>() }
    var fontSize by remember { mutableStateOf(sharedPreferences.getFloat("fontSize", 20f)) }
    var blink by remember { mutableStateOf(sharedPreferences.getBoolean("blink", true)) }
    var showText by remember { mutableStateOf(true) }
    var offsetX = remember { Animatable(0f) }
    var currentIndex by remember { mutableStateOf(0) }
    var animating by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val savedList = sharedPreferences.getString("textList", null)
        if (!savedList.isNullOrEmpty()) {
            textList.addAll(savedList.split("@_@").filter { it.isNotEmpty() })
        }
    }

    LaunchedEffect(animating, Unit) {
        while (isActive && animating) {
            if (textList.isNotEmpty()) {
                if (currentIndex < textList.size - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }
            }
            offsetX.animateTo(
                targetValue = boxW.toFloat(),
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
            offsetX.snapTo(-textW.toFloat())

        }
    }
    LaunchedEffect(textList.size, blink, fontSize) {
        sharedPreferences.edit().apply {
            putFloat("fontSize", fontSize)
            putString("textList", textList.joinToString("@_@"))
            putBoolean("blink", blink)
        }.apply()
    }

    LaunchedEffect(blink) {
        showText = true
        while (blink) {
            showText = !showText
            delay(500)
        }
    }

    if (open) {
        ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
            open = false
        }) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
                    .imePadding()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Font Size")
                    Spacer(Modifier.width(20.dp))
                    Slider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange = 20f..60f
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Blink")
                    Switch(checked = blink, onCheckedChange = { blink = it })
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                Spacer(Modifier.height(10.dp))
                Button(onClick = {
                    textList.add(text)
                    text = ""
                }, modifier = Modifier.fillMaxWidth()) { Text("Add") }
                Spacer(Modifier.height(10.dp))
                LazyColumn(
                    Modifier
                        .padding(bottom = 10.dp)
                        .height(200.dp)
                ) {
                    itemsIndexed(textList) { index, item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item)
                            IconButton(onClick = {
                                if (currentIndex == index) {
                                    currentIndex = 0
                                }
                                textList.removeAt(index)
                            }) {
                                Icon(
                                    Icons.Rounded.Delete,
                                    contentDescription = ":",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        HorizontalDivider()
                    }
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
            }
            .clickable {
                scope.launch {
                   animating = !animating
                }
            }
    ) {
        IconButton(
            onClick = {
                open = true
            },
            modifier = Modifier.align(Alignment.TopStart)
        ) { Icon(Icons.Rounded.Settings, contentDescription = "") }
        if (showText) {
            Text(if (textList.isEmpty()) "請輸入文字" else textList.get(currentIndex),
                fontSize = fontSize.sp,
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
}