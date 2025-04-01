package com.example.skillsdaantest

import androidx.annotation.RestrictTo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun Login(nav: NavController = rememberNavController()) {
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf<String>("") }
    var password by rememberSaveable { mutableStateOf<String>("") }
    var passwordCheck by rememberSaveable { mutableStateOf<String>("") }
    var showError by rememberSaveable { mutableStateOf(false) }
    var showErrorReason by rememberSaveable { mutableStateOf<String>("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 50.dp)
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(R.drawable.header_logo),
            contentDescription = "header logo",
            modifier = Modifier.height(70.dp)
        )
        Sh(100.dp)
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "登\t\t\t\t入",
                    color = Color(0xFF123617f),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Sh(50.dp)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.mail_blue),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF123617f)
                        )
                        Sw(10.dp)
                        BasicTextField(
                            value = email,
                            onValueChange = { email = it },
                            singleLine = true,
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                    }
                    Sh(5.dp)
                    HorizontalDivider()
                }
                Sh(50.dp)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.lock_blue),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF123617f)
                        )
                        Sw(10.dp)
                        BasicTextField(
                            value = password,
                            onValueChange = { password = it },
                            singleLine = true,
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                    }
                    Sh(5.dp)
                    HorizontalDivider()
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                if (showError) {
                    Text(
                        showErrorReason,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Red)
                            .padding(10.dp)
                    )
                    Sh(10.dp)
                }
                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            scope.launch {
                                showError = true
                                showErrorReason = "請輸入密碼或電子郵件"
                                delay(2000)
                                showError = false
                            }
                        }
//                        else if (!password.matches("^*@*.*".toRegex())) {
//                            scope.launch {
//                                showError = true
//                                showErrorReason = "電子郵件格式錯誤"
//                                delay(2000)
//                                showError = false
//                            }
//                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF123617f)),
                    shape = RoundedCornerShape(20)
                ) { Text("登\t\t入") }
                Sh(50.dp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("沒有任何帳號?")
                    Sw(1.dp)
                    TextButton(onClick = {
                        nav.navigate("signup")
                    }) {
                        Text("註冊", color = Color(0xFF19aa9d))
                    }
                }
            }
        }

    }
}
