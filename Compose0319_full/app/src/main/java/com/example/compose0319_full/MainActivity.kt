package com.example.compose0319_full

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose0319_full.ui.theme.Compose0319_fullTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose0319_fullTheme {
                Route()
            }
        }
    }
}

@Composable
fun Route() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Nav.Player.name) {
        composable(Nav.Home.name) {
            HomeScreen()
        }
        composable(Nav.Player.name) {
            PlayerScreen()
        }
    }

}

enum class Nav {
    Home, Player
}
