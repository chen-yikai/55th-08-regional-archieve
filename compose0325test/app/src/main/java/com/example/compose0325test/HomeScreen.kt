package com.example.compose0325test

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Home(nav: NavController) {
    var data by remember { mutableStateOf<List<MusicApi>>(emptyList()) }
    var openSetting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            data = fetchApi()
        }
    }

    if (openSetting) {
        ModalBottomSheet(onDismissRequest = { openSetting = false }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Setting", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Sh()
                FilledTonalButton(onClick = {
                    setAlarm(context, System.currentTimeMillis() + 5000)
                }) {
                    Text("Send a reminder")
                }
            }
        }
    }

    LazyColumn {
        stickyHeader {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    openSetting = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.setting),
                        contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    nav.navigate(Screens.player.name)
                }) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = ""
                    )
                }
            }
        }
        items(data) {
            Row(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                NetworkImage(hostname + it.cover, 70.dp) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.size(70.dp).clip(RoundedCornerShape(10.dp))
                    )
                }
                Sw(10.dp)
                Column {
                    Text(it.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(it.description, fontSize = 15.sp)
                }
            }
        }
    }
}