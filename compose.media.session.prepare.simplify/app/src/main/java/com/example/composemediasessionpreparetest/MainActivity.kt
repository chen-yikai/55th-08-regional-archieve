package com.example.composemediasessionpreparetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.composemediasessionpreparetest.ui.theme.ComposemediasessionpreparetestTheme
import android.content.ComponentName
import android.content.Intent
import com.example.myapp.MediaPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startForegroundService(Intent(this, PlaybackService::class.java))
        setContent {
            ComposemediasessionpreparetestTheme {
                MediaPlayerScreen()
            }
        }
    }
}