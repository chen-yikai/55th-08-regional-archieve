package com.example.skills53dic.components

import android.content.Context
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.example.skills53dic.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

data class SubNavLinksList(
    val title: String,
    val route: String,
)

data class NavLinksList(
    val title: String, val route: String, val sub: List<SubNavLinksList> = emptyList()
) {
    constructor() : this("", "", listOf())
}

@Composable
fun DrawerContent(drawerState: DrawerState, nav: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navLinksJson = context.assets.open("nav_links.json").bufferedReader().use { it.readText() }
    val navLinks = Gson().fromJson(navLinksJson, Array<NavLinksList>::class.java).toList()
    Row() {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .padding(start = 20.dp)
        ) {
            navLinks.forEach {
                Column(modifier = Modifier.padding(bottom = 5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .background(ColorBlue)
                        )
                        Sw(5.dp)
                        TextButton(onClick = {
                            if (it.route.isNotEmpty()) {
                                nav.navigate(it.route)
                                scope.launch {
                                    delay(200)
                                    drawerState.apply {
                                        close()
                                    }
                                }
                            }
                        }) {
                            LightGrayText(it.title, 20.sp, FontWeight.Medium)
                        }
                    }
                    if (it.sub.isNotEmpty()) {
                        Column(modifier = Modifier.padding(start = 20.dp)) {
                            it.sub.forEach {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_right),
                                        contentDescription = "Arrow right icon",
                                        modifier = Modifier.size(15.dp),
                                        tint = ColorGreen
                                    )
                                    Sw(5.dp)
                                    TextButton(onClick = {
                                        if (it.route.isNotEmpty()) {
                                            nav.navigate(it.route)
                                            scope.launch {
                                                delay(200)
                                                drawerState.apply {
                                                    close()
                                                }
                                            }
                                        }
                                    }) {
                                        LightGrayText(it.title, 15.sp, FontWeight.Normal)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
