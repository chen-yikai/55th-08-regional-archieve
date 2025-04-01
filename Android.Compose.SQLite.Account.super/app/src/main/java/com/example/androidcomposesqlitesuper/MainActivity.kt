package com.example.androidcomposesqlitesuper

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidcomposesqlitesuper.ui.theme.AndroidComposeSQLitesuperTheme

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
fun Main(viewModel: userAuth = viewModel()) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    if (sharedPreferences.getBoolean("rememberme", false)) {
        viewModel.changeStarterScreen("home")
    }
    Box(
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White, Color(0xFFefacff)
                )
            )
        )
    ) {
        NavHost(navController = navController, startDestination = viewModel.starterScreen.value) {
            composable("signin") {
                SignIn(nav = navController)
            }
            composable("signup") {
                SignUp(nav = navController)
            }
            composable("home") {
                Home(nav= navController)
            }
        }
    }
}
