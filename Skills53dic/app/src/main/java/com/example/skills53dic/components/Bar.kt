package com.example.skills53dic.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skills53dic.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun TopBar(scope: CoroutineScope, drawerState: DrawerState, nav: NavController) {
    val currentRoute = nav.currentDestination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .animateContentSize()
            .height(if (currentRoute == "splash_screen" || currentRoute == "media_center_detail" || currentRoute == "add_ticket" || currentRoute == "buy_ticket" || currentRoute == "signin" || currentRoute == "signup") 0.dp else 50.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.menu), contentDescription = "App Menu"
            )
        }
        Image(painter = painterResource(id = R.drawable.horizontal_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(
                    if (currentRoute == "home") Alignment.TopCenter else Alignment.TopStart
                )
                .padding(
                    start = if (currentRoute == "home") 0.dp else 40.dp
                )
                .clickable {
                    nav.navigate("home")
                })
    }
}
