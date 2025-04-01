package com.example.skills53dic.screens

import android.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.AddTicketViewModel
import com.example.skills53dic.R
import com.example.skills53dic.components.AddTicketInput
import com.example.skills53dic.components.BlueText
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.CustomButton
import com.example.skills53dic.components.CustomTextButton
import com.example.skills53dic.components.LightGrayText
import com.example.skills53dic.components.Sh
import com.example.skills53dic.components.Sw
import com.example.skills53dic.db.TicketsSchema
import com.example.skills53dic.db.TicketsViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun AddTicket(
    nav: NavController = rememberNavController(),
    db: TicketsViewModel = viewModel(),
    viewModel: AddTicketViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val option = arrayOf("ticket_1.png", "ticket_2.png", "ticket_3.png")
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var imageShow by remember { mutableStateOf<Boolean>(false) }

    Scaffold(topBar = {
        AddTicketTopBar(nav)
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sh(20.dp)
                Box(modifier = Modifier
                    .size((LocalConfiguration.current.screenWidthDp * 0.5).dp)
                    .border(1.dp, Color(0xFFa3a3a3), RoundedCornerShape(10.dp))
                    .background(Color(0xFFf7f7f7))
                    .clickable {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("選擇測試圖片")
                        builder.setSingleChoiceItems(option, -1) { _, which ->
                            selectedOption = option[which]
                        }
                        builder.setPositiveButton("確定") { dialog, _ ->
                            if (selectedOption != null) {
                                viewModel.qrcodeRead(context, "qrcode/$selectedOption")
                                imageShow = true
                                toast("成功自動填入${selectedOption}的資料", context)
                                dialog.dismiss()
                            }
                        }
                        builder.setNegativeButton("取消") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }) {
                    if (imageShow) {
                        ImageAsset("qrcode/$selectedOption")
                    } else {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Icon(
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = "add ticket by scanning qrcode",
                                tint = ColorBlue,
                                modifier = Modifier
                                    .size(90.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            LightGrayText("匯入票卡QRCode", 15.sp, FontWeight.Bold)
                        }
                    }

                }
                Sh(20.dp)
                AddTicketInput(viewModel.type, "票種")
                AddTicketInput(viewModel.name, "姓名")
                AddTicketInput(viewModel.email, "Email")
                AddTicketInput(viewModel.phone, "電話")
                AddTicketInput(viewModel.date, "日期")
                AddTicketInput(viewModel.ticketId, "票卡號碼")
                AddTicketInput(viewModel.price, "價格")
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    CustomTextButton("取消") {
                        nav.navigate("tickets")
                    }
                    Sw(5.dp)
                    CustomButton("匯入") {
                        scope.launch {
                            if (!db.checkSameId(viewModel.ticketId.value)) {
                                db.add(
                                    TicketsSchema(
                                        id = viewModel.ticketId.value,
                                        type = viewModel.type.value,
                                        name = viewModel.name.value,
                                        email = viewModel.email.value,
                                        phone = viewModel.phone.value,
                                        date = viewModel.date.value,
                                        price = viewModel.price.value,
                                    )
                                )
                            } else {
                                toast("票卡加入失敗: 此票卡已存在", context)
                            }
                            nav.navigate("tickets")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTicketTopBar(nav: NavController = rememberNavController()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            nav.navigate("tickets")
        }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "go back",
                tint = ColorBlue
            )
        }
        BlueText("匯入票卡", 20.sp, FontWeight.Bold)
    }
}