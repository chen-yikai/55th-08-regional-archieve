package com.example.androidcomposesqlitesuper

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun Br(height: Int = 10) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun Wr(width: Int = 5) {
    Spacer(modifier = Modifier.width(width.dp))
}

@Composable
fun GreetInput(value: MutableState<String>, label: String) {
    BasicTextField(
        value = value.value,
        onValueChange = {
            value.value = it
        },
        modifier = Modifier
            .border(
                1.5.dp, Color.Black, RoundedCornerShape(8.dp)
            )
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .fillMaxWidth(),
        decorationBox = { innerTextField ->
            Box() {
                if (value.value.isEmpty()) {
                    Text(label)
                }
                innerTextField()
            }
        },
        singleLine = true,
    )
}
