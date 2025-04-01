package com.example.skillstestcompose53regionalpre

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun About() {
    Column(Modifier.fillMaxSize()) {
        Title("關於展館")
        Sh()
        AndroidView(factory = {
            WebView(it).apply {
                loadUrl("file:///android_asset/html/info/index.html")
            }
        })
    }
}