package com.example.compsoe0324player_service_prepare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.compsoe0324player_service_prepare.ui.theme.Compsoe0324player_service_prepareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Compsoe0324player_service_prepareTheme {
                PlayerScreen(intent.getIntExtra("index", 0))
            }
        }
    }
}