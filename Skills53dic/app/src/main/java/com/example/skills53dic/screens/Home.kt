package com.example.skills53dic.screens

import android.content.Context
import android.view.Surface
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.MediaCenterDetailViewModel
import com.example.skills53dic.R
import com.example.skills53dic.components.BlackText
import com.example.skills53dic.components.ColorBlack
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorGreen
import com.example.skills53dic.components.LightGrayText
import com.example.skills53dic.components.SafeColumn
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(nav: NavController, mediaCenterDetailViewModel: MediaCenterDetailViewModel) {
    var scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Gallery()
        MediaCenter(nav, mediaCenterDetailViewModel)
        TicketInfo(nav)
    }
}

@Preview(showBackground = true)
@Composable
fun TicketInfo(nav: NavController = rememberNavController()) {
    SafeColumn {
        BlackText("購票資訊", 20.sp, FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .border(1.dp, ColorBlack, RoundedCornerShape(5))
                .fillMaxWidth()
                .height(120.dp)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    BlackText("2023第41屆新一代設計展", 18.sp, FontWeight.ExtraBold)
                    BlackText("YODEX", 18.sp, FontWeight.Normal)
                }
                Column(modifier = Modifier.fillMaxHeight()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            nav.navigate("buy_ticket")
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        modifier = Modifier
                            .border(
                                1.dp, ColorBlack, RoundedCornerShape(100)
                            )
                            .size(100.dp, 40.dp)
                    ) {
                        BlackText("購票", 16.sp, FontWeight.Normal)
                    }
                }
            }
        }
    }
}

data class MediaCenter(
    val title: String, val dateTime: String, val hall: List<String>, val content: String
)

@Composable
fun MediaCenter(
    nav: NavController,
    viewModel: MediaCenterDetailViewModel
) {
    val context = LocalContext.current
    val content = context.assets.open("media_center.json").bufferedReader().use { it.readText() }
    val mediaCenter = Gson().fromJson(content, Array<MediaCenter>::class.java).toList()

    SafeColumn {
        BlackText(text = "媒體中心", 20.sp, FontWeight.Bold)
        Spacer(modifier = Modifier.height(5.dp))
        mediaCenter.forEach {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        viewModel.setData(it)
                        nav.navigate("media_center_detail")
                    }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.width(70.dp)
                ) {
                    LightGrayText("${it.dateTime.replace('.', '/')}", 15.sp, FontWeight.Medium)
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.Center) {
                        it.hall.forEach {
                            Text(
                                it,
                                color = if (it == "1館") ColorBlue else ColorGreen,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))
                BlackText(it.title, 16.sp, FontWeight.Bold)
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Gallery() {
    val pager_images = listOf(
        R.drawable.pager_1,
        R.drawable.pager_2,
    )
    var pager_state = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Column {
        HorizontalPager(
            state = pager_state,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        ) { page ->
            Image(
                painter = painterResource(pager_images[page]),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Image of page $page"
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 20.dp, top = 5.dp)
        ) {
            repeat(2) { i ->
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (pager_state.currentPage == i) Color.Gray else Color.LightGray)
                        .height(12.dp)
                        .animateContentSize()
                        .width(if (pager_state.currentPage == i) 35.dp else 12.dp)
                        .clickable {
                            scope.launch {
                                pager_state.animateScrollToPage(i)
                            }
                        }
                )
            }
        }
    }
}