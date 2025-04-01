package com.example.skillstestcompose53regionalpre

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text

@Preview(showBackground = true)
@Composable
fun ContactUs() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Title("聯絡我們")
        Sh()
        Column(Modifier.padding(horizontal = 20.dp)) {
            ContentInput(name, "姓名") { name = it }
            ContentInput(email, "電子信箱") { email = it }
            ContentInput(content, "內容") { content = it }
            Button(onClick = {}) { }
        }
    }
}

@Composable
fun ContentInput(value: String, info: String, onValueChange: (String) -> Unit) {
    Column(Modifier.padding(vertical = 5.dp)) {
        Text(info)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .border(1.dp, Color(0xff447dbe))
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
        )
    }
}