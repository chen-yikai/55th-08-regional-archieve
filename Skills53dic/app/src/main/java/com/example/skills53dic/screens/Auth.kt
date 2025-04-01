package com.example.skills53dic.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skills53dic.R
import com.example.skills53dic.components.AuthInput
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorGreen
import com.example.skills53dic.components.CustomButton
import com.example.skills53dic.components.GreenText
import com.example.skills53dic.components.Sw
import com.example.skills53dic.db.UsersSchema
import com.example.skills53dic.db.UsersViewModel
import kotlinx.coroutines.launch
import kotlin.text.Regex

@Preview(showBackground = true)
@Composable
fun SignUp(nav: NavController = rememberNavController(), db: UsersViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordConfirm = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 50.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.horizontal_logo),
            contentDescription = "A logo",
            modifier = Modifier.height(60.dp)
        )
        Column() {
            AuthInput(R.drawable.mail_green, email, "輸入Email")
            AuthInput(R.drawable.locl_green, password, "輸入密碼", true)
            AuthInput(R.drawable.locl_green, passwordConfirm, "確認密碼", true)
        }
        CustomButton("註冊", color = ColorGreen, modifier = Modifier.fillMaxWidth()) {
            scope.launch {
                if (!Regex("^(?=.{1,30}$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$").matches(
                        email.value
                    )
                ) {
                    toast("Email格式錯誤", context)
                } else if (!Regex("^[A-Za-z0-9]{6,}$").matches(password.value)) {
                    toast("密碼格式錯誤", context)
                } else if (email.value.isEmpty() || password.value.isEmpty() || passwordConfirm.value.isEmpty()) {
                    toast("請輸入完整資訊", context)
                } else if (password.value.contains(" ") || passwordConfirm.value.contains(" ") || email.value.contains(
                        " "
                    )
                ) {
                    toast("請勿輸入空白字元", context)
                } else if (password.value != passwordConfirm.value) {
                    toast("密碼與確認密碼不一致", context)
                } else {
                    if (!db.checkSameEmail(email.value)) {
                        db.add(UsersSchema(email.value, password.value))
                        toast("註冊成功", context)
                        nav.navigate("signin")
                    } else {
                        toast("該Email以被註冊", context)
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("已經有帳號了!")
            TextButton(onClick = {
                nav.navigate("signin")
            }) { Text("登入", color = ColorGreen) }
        }
    }
}

@Composable
fun SignIn(nav: NavController = rememberNavController(), db: UsersViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 50.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.horizontal_logo),
            contentDescription = "A logo",
            modifier = Modifier.height(60.dp)
        )
        Column() {
            AuthInput(R.drawable.mail_green, email, "輸入Email")
            AuthInput(R.drawable.locl_green, password, "輸入密碼", true)
        }
        CustomButton("登入", modifier = Modifier.fillMaxWidth()) {
            scope.launch {
                if (!Regex("^(?=.{1,30}$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$").matches(
                        email.value
                    )
                ) {
                    toast("Email格式錯誤", context)
                } else if (!Regex("^[A-Za-z0-9]{6,}$").matches(password.value)) {
                    toast("密碼格式錯誤", context)
                } else if (email.value.isEmpty() || password.value.isEmpty()) {
                    toast("請輸入完整資訊", context)
                } else if (password.value.contains(" ") || email.value.contains(
                        " "
                    )
                ) {
                    toast("請勿輸入空白字元", context)
                } else {
                    scope.launch {
                        if (db.authSignIn(email.value, password.value)) {
                            toast("登入成功", context)
                            nav.navigate("splash_screen")
                        } else {
                            toast("登入失敗", context)
                        }

                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("沒有任何帳號嗎?")
            TextButton(onClick = {
                nav.navigate("signup")
            }) { Text("註冊", color = ColorBlue) }
        }
    }
}