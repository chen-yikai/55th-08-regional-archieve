package com.example.androidcomposenavhostlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidcomposenavhostlearning.ui.theme.AndroidComposeNavHostlearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

var navController: NavHostController? = null

@Composable
fun Main() {
    navController = rememberNavController()
    NavHost(navController = navController!!, startDestination = "home") {
        composable("home") {
            Home();
        }
        composable("about") {
            About();
        }
    }
}

@Composable
fun Home() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home", fontSize = 40.sp)
        Button(onClick = {
            navController?.navigate("about")
        }) { Text("Go to About") }
    }
}

@Composable
fun About() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("About", fontSize = 40.sp)
        Button(onClick = {
            navController!!.popBackStack()
        }) { Text("Go Back") }
    }
}