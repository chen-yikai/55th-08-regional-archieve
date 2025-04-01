package com.example.compose0325test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose0325test.ui.theme.Compose0325testTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose0325testTheme {
                Route()
            }
        }
    }
}

@Composable
fun Route() {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Screens.home.name,
        modifier = Modifier.statusBarsPadding()
    ) {
        composable(Screens.player.name) {
            Player(nav)
        }
        composable(Screens.home.name) {
            Home(nav)
        }
    }
}