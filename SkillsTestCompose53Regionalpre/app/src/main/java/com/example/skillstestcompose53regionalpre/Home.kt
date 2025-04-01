package com.example.skillstestcompose53regionalpre

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson

@Preview(showBackground = true)
@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
        Pager()
        News()
    }
}

@Preview(showBackground = true)
@Composable
fun Pager() {
    val pagerImage = arrayOf(R.drawable.pager_1, R.drawable.pager_2)
    val pagerState = rememberPagerState { 2 }
    HorizontalPager(state = pagerState) { page ->
        Image(
            painter = painterResource(pagerImage[page]),
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentDescription = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun News() {
    val context = LocalContext.current

    data class MediaCenter(
        val title: String, val dateTime: String, val hall: List<String>, val content: String
    )

    val file = context.assets.open("media_center.json").bufferedReader().use { it.readText() }
    val jsonData = Gson().fromJson(file, Array<MediaCenter>::class.java).toList()
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Title("最新消息")
        LazyColumn(Modifier.padding(horizontal = 20.dp)) {
            items(jsonData) {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.dateTime, color = Color.Gray)
                        Row {
                            it.hall.forEach { hell ->
                                Text(
                                    hell,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(start = 5.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (hell == "1館") Color(0xff1aab9f) else Color(
                                                0xff447dbe
                                            )
                                        )
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    Row {
                        Text(it.title)
                    }
                }
                Sh(5.dp)
                HorizontalDivider()
                Sh(5.dp)
            }
        }
    }
}
