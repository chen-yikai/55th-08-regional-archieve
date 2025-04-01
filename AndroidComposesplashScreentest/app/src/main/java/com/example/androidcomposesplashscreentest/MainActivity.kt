package com.example.androidcomposesplashscreentest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.androidcomposesplashscreentest.ui.theme.AndroidComposesplashScreentestTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComposesplashScreentestTheme {
                SplashScreen()
            }
        }
    }
}


@Composable
fun navMan() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("about") { About(navController) }
    }
}

@Composable
fun SplashScreen() {
    var isSplashVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        isSplashVisible = false
    }
    if (isSplashVisible) {
        SplashScreenContent()
    } else {
        navMan()
    }
}

@Composable
fun SplashScreenContent() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE494FF))
    ) {
        Text(
            text = "Splash Screen",
            fontSize = 40.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate("about")
        }) {
            Text(text = "Go to About")
        }
    }
}

@Composable
fun About(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "About Screen", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text(text = "Go to Home",Color = Color(0xFFE494FF))
        }
    }
}
