package com.example.skills53dic.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.BuyTicketViewModel
import com.example.skills53dic.R
import com.example.skills53dic.components.BlueText
import com.example.skills53dic.components.BuyBox
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorLightGray
import com.example.skills53dic.components.ContactInput
import com.example.skills53dic.components.CustomButton
import com.example.skills53dic.components.CustomTextButton
import com.example.skills53dic.components.LightGrayText
import com.example.skills53dic.components.SafeColumn
import com.example.skills53dic.components.Sh
import com.example.skills53dic.components.Sw
import com.example.skills53dic.components.TableFucker
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.example.skills53dic.components.BlackText
import com.example.skills53dic.db.TicketsSchema
import com.example.skills53dic.db.TicketsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicket(
    RootNav: NavController = rememberNavController(), db: TicketsViewModel = viewModel()
) {
    val BuyNav = rememberNavController()
    val viewModel = BuyTicketViewModel()
    Scaffold(topBar = {
        BuyTicketsTopBar(RootNav, BuyNav)
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(navController = BuyNav, startDestination = "ticket_type") {
                composable("ticket_type") {
                    TicketType(BuyNav, viewModel)
                }

                composable("ticket_detail") {
                    TicketDetail(viewModel, BuyNav, RootNav, db)
                }
            }

        }
    }
}

data class TicketTypeSchema(
    val ticketType: String, val price: Int
)

@Preview(showBackground = true)
@Composable
fun TicketType(
    nav: NavController = rememberNavController(), viewModel: BuyTicketViewModel = viewModel()
) {
    val context = LocalContext.current
    val gson = Gson()
    val dataJson: String = context.assets.open("ticket_type.json").use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.readText()
        }
    }
    val data = gson.fromJson(dataJson, Array<TicketTypeSchema>::class.java)

    SafeColumn {
        Column(
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .border(1.dp, ColorLightGray, RoundedCornerShape(10.dp))
                .background(
                    Color.White
                )
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableFucker("票種", ColorLightGray)
                TableFucker("價格", ColorLightGray)
                TableFucker(
                    "數量", ColorLightGray, customWidth = 120.dp, alignment = Alignment.Center
                )
            }
            data.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableFucker(item.ticketType)
                    TableFucker("NT " + item.price.toString())
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(120.dp)
                    ) {
                        IconButton(modifier = Modifier.size(40.dp, 40.dp), onClick = {
                            viewModel.setTicketTypeData(
                                index, (viewModel.ticketTypeData.value[index] - 1).toString()
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.minus),
                                contentDescription = "minus"
                            )
                        }
                        Box(
                            modifier = Modifier.size(40.dp, 30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextField(
                                value = viewModel.ticketTypeData.value[index].toString(),
                                onValueChange = {
                                    viewModel.setTicketTypeData(index, it)
                                },
                                modifier = Modifier.align(Alignment.Center),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                            )
                            HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
                        }
                        IconButton(modifier = Modifier.size(40.dp, 40.dp), onClick = {
                            viewModel.setTicketTypeData(
                                index, (viewModel.ticketTypeData.value[index] + 1).toString()
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.add),
                                contentDescription = "plus"
                            )
                        }
                    }
                }
            }
        }
        Sh(20.dp)
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            CustomButton("下一步", 20.dp, modifier = Modifier, onClick = {
                var total = 0
                viewModel.ticketTypeData.value.forEach {
                    total += it
                }
                if (total != 0) {
                    nav.navigate("ticket_detail")
                } else {
                    toast("請選擇至少一張票", context)
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TicketDetail(
    viewModel: BuyTicketViewModel = viewModel(),
    nav: NavController = rememberNavController(),
    rootNav: NavController = rememberNavController(),
    db: TicketsViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gson = Gson()
    val dataJson: String = context.assets.open("ticket_type.json").use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.readText()
        }
    }
    val paymentMethodsJson: String = context.assets.open("payment_method.json").use {
        BufferedReader(InputStreamReader(it)).use {
            it.readText()
        }
    }
    val data = gson.fromJson(dataJson, Array<TicketTypeSchema>::class.java)
    val paymentMethods = gson.fromJson(paymentMethodsJson, Array<String>::class.java)
    val totalCount = rememberSaveable { mutableStateOf<Int>(0) }
    var syntaxError = false
    val name = remember { mutableStateOf("") }
    var nameError = if (name.value.isEmpty()) {
        ""
    } else if (name.value.contains(" ")) {
        syntaxError = true
        "勿包含空白字元"
    } else {
        ""
    }
    val email = remember { mutableStateOf("") }
    var emailError = if (email.value.isEmpty()) {
        ""
    } else if (email.value.contains(" ")) {
        syntaxError = true
        "勿包含空白字元"
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
        syntaxError = true
        "格式錯誤"
    } else {
        ""
    }
    val phone = remember { mutableStateOf("") }
    var phoneError = if (phone.value.isEmpty()) {
        ""
    } else if (phone.value.contains(" ")) {
        syntaxError = true
        "勿包含空白字元"
    } else if (!Regex("^09[0-9]{8}$").matches(phone.value)) {
        syntaxError = true
        "格式錯誤"
    } else {
        ""
    }
    val payment = remember { mutableStateOf("") }
    var paymentDropDownShow = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val todayMillis = calendar.timeInMillis
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val today = dateFormat.format(Date(todayMillis))
    val showAlertDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            totalCount.value =
                data.sumOf { it.price * viewModel.ticketTypeData.value[data.indexOf(it)] }
        }
    }

    LaunchedEffect(showSuccessDialog.value) {
        if (showSuccessDialog.value == true) {
            showAlertDialog.value = false
            showSuccessDialog.value = true
            delay(2000)
            showSuccessDialog.value = false
            rootNav.navigate("tickets")
        }
    }

    if (showDatePicker.value) {
        DatePickerDialog(onDismissRequest = { showDatePicker.value = false }, confirmButton = {
            Button(onClick = {
                val milliseconds = datePickerState.selectedDateMillis
                val userFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val tempSelectDate = dateFormat.format(Date(milliseconds!!))
                if (tempSelectDate < today) {
                    toast("請輸入今日以後的日期", context)
                } else {
                    selectedDate.value = userFormat.format(Date(milliseconds))
                    showDatePicker.value = false
                }
            }) {
                Text("OK")
            }
        }, dismissButton = {
            Button(onClick = {
                showDatePicker.value = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(
                state = datePickerState,
            )
        }
    }

    SafeColumn {
        Column(
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .border(1.dp, ColorLightGray, RoundedCornerShape(10.dp))
                .background(
                    Color.White
                )
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableFucker("票種", ColorLightGray)
                TableFucker("票種", ColorLightGray)
                TableFucker("數量", ColorLightGray)
            }
            LazyColumn {
                itemsIndexed(data) { index, item ->
                    if (viewModel.ticketTypeData.value[index] != 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TableFucker(item.ticketType)
                            TableFucker("NT " + item.price.toString())
                            TableFucker(viewModel.ticketTypeData.value[index].toString())
                        }
                    }
                }
            }
            Sh(5.dp)
            HorizontalDivider()
            Sh(5.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LightGrayText("總額", 15.sp)
                BlueText("NT " + totalCount.value.toString(), 15.sp)
            }
        }
        Sh(20.dp)
        Text("購買人資料", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Sh(10.dp)
        Column {
            ContactInput(name, "姓名", errorMessage = nameError) {
                if (it.length <= 15) {
                    name.value = it
                } else {
                    toast("姓名不可超過15字元", context)
                }
            }
            ContactInput(email, "電子郵件", errorMessage = emailError) {
                if (it.length <= 30) {
                    email.value = it
                } else {
                    toast("Email不可超過30字元", context)
                }
            }
            ContactInput(phone, "電話", errorMessage = phoneError) {
                phone.value = it
            }
            BuyBox(text = selectedDate, label = "日期") {
                showDatePicker.value = true
            }
            Column() {
                BuyBox(text = payment, label = "付款方式") {
                    paymentDropDownShow.value = true
                }
                DropdownMenu(
                    expanded = paymentDropDownShow.value,
                    onDismissRequest = { paymentDropDownShow.value = false },
                ) {
                    paymentMethods.forEach {
                        DropdownMenuItem(text = {
                            Text(it, fontSize = 15.sp)
                        }, onClick = {
                            payment.value = it
                            paymentDropDownShow.value = false
                        })
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                CustomTextButton("上一步") {
                    nav.popBackStack()
                }
                Sw(10.dp)
                CustomButton("確認購買", padding = 10.dp) {
                    if (name.value.isEmpty() || email.value.isEmpty() || phone.value.isEmpty() || selectedDate.value.isEmpty() || payment.value.isEmpty()) {
                        toast("請填寫完整表單", context)
                    } else if (syntaxError) {
                        toast("表單格式有誤", context)
                    } else {
                        showAlertDialog.value = true
                    }
                }
            }
        }
    }
    if (showAlertDialog.value) {
        AlertDialogChecker(
            totalCount.value,
            payment.value,
            dismiss = { showAlertDialog.value = false },
        ) {
            scope.launch {
                data.forEachIndexed { index, item ->
                    val ticketTypeData = viewModel.ticketTypeData.value[index]
                    if (ticketTypeData != 0) {
                        for (count in 1..ticketTypeData) {
                            val uuid = UUID.randomUUID().toString().slice(0..10).let {
                                "TK-$it"
                            }
                            db.add(
                                TicketsSchema(
                                    id = uuid,
                                    type = item.ticketType,
                                    name = name.value,
                                    email = email.value,
                                    phone = phone.value,
                                    price = item.price.toString(),
                                    date = selectedDate.value
                                )
                            )
                        }
                    }
                }
                showSuccessDialog.value = true
            }
        }
    }
    if (showSuccessDialog.value) {
        SuccessDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AlertDialogChecker(
    total: Int = 0,
    paymentMethod: String = "Google Pay",
    dismiss: () -> Unit = {},
    confirm: () -> Unit = {},
) {
    BasicAlertDialog(onDismissRequest = { dismiss() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackText("總金額")
                BlackText("NT $total")
            }
            Sh(10.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackText("付款方式")
                BlackText(paymentMethod)
            }
            Sh(20.dp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                CustomTextButton("取消") {
                    dismiss()
                }
                CustomButton("確認") {
                    confirm()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SuccessDialog() {
    BasicAlertDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "check image",
                modifier = Modifier.height(160.dp)
            )
            Sh(30.dp)
            Text(
                text = "購買成功",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BuyTicketsTopBar(
    RootNav: NavController = rememberNavController(),
    BuyNav: NavController = rememberNavController()
) {
    val navBackStackEntry by BuyNav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            if (currentRoute == "ticket_type") {
                RootNav.navigate("home")
            } else {
                BuyNav.navigate("ticket_type")
            }
        }, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                painter = painterResource(R.drawable.arrow_left),
                tint = ColorBlue,
                contentDescription = "Back"
            )
        }
        BlueText(
            "2023第41屆新一代設計展",
            weight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}