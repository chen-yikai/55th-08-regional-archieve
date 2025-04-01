package com.example.skills53dic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.components.DrawerContent
import com.example.skills53dic.components.TopBar
import com.example.skills53dic.db.TicketsViewModel
import com.example.skills53dic.db.UsersViewModel
import com.example.skills53dic.db.getDataBase
import com.example.skills53dic.screens.AboutInfo
import com.example.skills53dic.screens.AboutOperator
import com.example.skills53dic.screens.AddTicket
import com.example.skills53dic.screens.BuyTicket
import com.example.skills53dic.screens.Contact
import com.example.skills53dic.screens.Floor3d
import com.example.skills53dic.screens.Home
import com.example.skills53dic.screens.MediaCenterDetail
import com.example.skills53dic.screens.PublicArt
import com.example.skills53dic.screens.SignIn
import com.example.skills53dic.screens.SignUp
import com.example.skills53dic.screens.Tickets
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavHoster()
        }
    }
}

@Composable
fun NavHoster(
    mediaCenterDetailViewModel: MediaCenterDetailViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination
    val DataBase = getDataBase(LocalContext.current)
    val ticketsViewModel = TicketsViewModel(DataBase)
    val usersViewModel = UsersViewModel(DataBase)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerState, navController) },
        gesturesEnabled = false
    ) {
        Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars), topBar = {
            TopBar(scope, drawerState, navController)
        }) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        Home(navController, mediaCenterDetailViewModel)
                    }
                    composable("media_center_detail") {
                        MediaCenterDetail(navController, mediaCenterDetailViewModel)
                    }
                    composable("about_operator") {
                        AboutOperator()
                    }
                    composable("about_info") {
                        AboutInfo()
                    }
                    composable("signin") {
                        SignIn(navController, usersViewModel)
                    }
                    composable("signup") {
                        SignUp(navController, usersViewModel)
                    }
                    composable("floor_3d") {
                        Floor3d()
                    }
                    composable("contact") {
                        Contact(navController)
                    }
                    composable("tickets") {
                        Tickets(navController, ticketsViewModel)
                    }
                    composable("add_ticket") {
                        AddTicket(navController, ticketsViewModel)
                    }
                    composable("buy_ticket") {
                        BuyTicket(navController, ticketsViewModel)
                    }
                    composable("public_art") {
                        PublicArt()
                    }
                    composable("splash_screen") {
                        SplashScreen(navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreen(nav: NavController = rememberNavController()) {
    var showSplashScreen by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        nav.navigate("home")
    }
    if (showSplashScreen) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.splash_screen),
                contentDescription = "Splash Screen",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}