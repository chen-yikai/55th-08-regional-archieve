package com.example.skillstestcompose53regionalpre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillstestcompose53regionalpre.ui.theme.SkillsTestCompose53RegionalpreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillsTestCompose53RegionalpreTheme {
                NavHoster()
            }
        }
    }
}

@Composable
fun NavHoster() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { Drawer(navController, drawerState) },
        gesturesEnabled = false
    ) {
        Scaffold(topBar = {
            AppBar(drawerState)
        }) { innerPadding ->
            val pd = PaddingValues(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
            Box(Modifier.padding(pd)) {
                NavHost(startDestination = "home", navController = navController) {
                    composable("home") {
                        Home()
                    }
                    composable("about") {
                        About()
                    }
                    composable("floorMap") {
                        FloorMap()
                    }
                    composable("publicArt") {
                        PublicArt()
                    }
                    composable("contactUs") {
                        ContactUs()
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            scope.launch {
                drawerState.open()
            }
        }) {
            Icon(painter = painterResource(R.drawable.menu), contentDescription = "")
        }
    }
}

@Composable
fun Drawer(nav: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color(0xffffffff)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val link = DrawerNavButton(nav, drawerState)
        link.button("首頁", "home")
        link.button("樓層立體圖", "floorMap")
        link.button("工共藝術", "publicArt")
        link.button("關於展館", "about")
        link.button("聯絡我們", "contactUs")
    }
}