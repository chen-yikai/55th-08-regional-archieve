package com.example.skills53dic.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.R
import com.example.skills53dic.components.BlackText
import com.example.skills53dic.components.BlueText
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorLightGray
import com.example.skills53dic.components.LightGrayText
import com.example.skills53dic.components.SafeColumn
import com.example.skills53dic.components.Sh
import com.example.skills53dic.db.TicketsViewModel

@Composable
fun Tickets(nav: NavController = rememberNavController(), db: TicketsViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Scaffold(floatingActionButton = {
        IconButton(modifier = Modifier
            .padding(5.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .background(Color.White), onClick = { nav.navigate("add_ticket") }) {
            Icon(
                painter = painterResource(R.drawable.add),
                tint = ColorBlue,
                contentDescription = "Add new tickets",
                modifier = Modifier.size(30.dp)
            )
        }
    }) { innerpadding ->
        SafeColumn {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerpadding)
            ) {
                if (db.tickets.isNotEmpty()) {
                    db.tickets.forEach {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth()
                                .height(150.dp)
                                .border(1.dp, ColorLightGray, RoundedCornerShape(10.dp))
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                        ) {
                            Column(modifier = Modifier.align(Alignment.TopStart)) {
                                BlueText(it.title)
                            }
                            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                                LightGrayText(it.name, weight = FontWeight.Bold)
                                LightGrayText(it.id, size = 15.sp)
                                Sh(5.dp)
                                LightGrayText(it.date.replace(".", "/"), size = 13.sp)
                            }
                            Column(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                horizontalAlignment = Alignment.End
                            ) {
                                LightGrayText(
                                    it.type, size = 20.sp, weight = FontWeight.Bold
                                )
                                LightGrayText(
                                    "NT " + it.price, size = 20.sp, weight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Sh(60.dp)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            LightGrayText("無票卡", size = 20.sp, weight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TicketBoxPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, ColorLightGray, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            BlueText("helloworld")
        }
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            LightGrayText("陳奕愷", weight = FontWeight.Bold)
            LightGrayText("redfirjgiurejgjfudjj", size = 15.sp)
            Sh(5.dp)
            LightGrayText("2023.01.01", size = 13.sp)
        }
        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
            LightGrayText("一日票", size = 20.sp, weight = FontWeight.ExtraBold)
            LightGrayText("NT 320", size = 20.sp, weight = FontWeight.ExtraBold)
        }
    }
}