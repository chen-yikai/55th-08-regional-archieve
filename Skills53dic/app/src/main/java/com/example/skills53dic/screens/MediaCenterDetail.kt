package com.example.skills53dic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.MediaCenterDetailViewModel
import com.example.skills53dic.components.BlackText
import com.example.skills53dic.R
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorGreen
import com.example.skills53dic.components.LightGrayText
import com.example.skills53dic.components.Sh
import com.example.skills53dic.components.Sw

@Preview(showBackground = true)
@Composable
fun MediaCenterDetail(
    nav: NavController = rememberNavController(),
    viewModel: MediaCenterDetailViewModel = viewModel()
) {
    var data = viewModel.data.value
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .background(Color.White)) {
                IconButton(onClick = {
                    nav.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "Back"
                    )
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                BlackText(data?.title.toString(), 25.sp, FontWeight.Bold)
                Sh(10.dp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        data?.hall?.forEach {
                            Text(
                                it,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = if (it == "1館") ColorBlue else ColorGreen
                            )
                            Sw(5.dp)
                        }
                    }
                    LightGrayText(
                        "發文日期: ${data?.dateTime?.replace(".", "/")}",
                        15.sp,
                        FontWeight.Normal
                    )
                }
                Sh(20.dp)
                LightGrayText(data?.content.toString())
            }
        }
    }
}
