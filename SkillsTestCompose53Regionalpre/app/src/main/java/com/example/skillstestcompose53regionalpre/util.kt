package com.example.skillstestcompose53regionalpre

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp

class DrawerNavButton(
    val nav: NavController, val drawerState: DrawerState
) {
    @Composable
    fun button(text: String, link: String) {
        val scope = rememberCoroutineScope()
        TextButton(onClick = {
            nav.navigate(link)
            scope.launch {
                drawerState.close()
            }
        }) {
            Text(
                text, color = Color(0xff1aab9f), fontWeight = FontWeight.Bold, fontSize = 30.sp
            )
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        color = Color(0xff1aab9f),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp, modifier = Modifier.padding(start = 20.dp)
    )
}

@Composable
fun Sw(w: Dp = 20.dp) {
    Spacer(Modifier.width(w))
}
@Composable
fun Sh(h: Dp = 20.dp) {
    Spacer(Modifier.height(h))
}