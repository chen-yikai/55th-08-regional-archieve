package com.example.skills53dic.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

val ColorLightGray = Color(0xFF707070)
val ColorBlue = Color(0xFF11617F)
val ColorGreen = Color(0xFF1AAB9F)
val ColorBlack = Color(0xFF333333)

@Composable
fun BlackText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
) {
    Text(text, color = Color(0xFF333333), fontSize = size, fontWeight = weight)
}

@Composable
fun LightGrayText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Text(text, color = Color(0xFF707070), fontSize = size, fontWeight = weight, modifier = modifier)
}


@Composable
fun BlueText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Text(text, color = Color(0xFF11617F), fontSize = size, fontWeight = weight, modifier = modifier)
}

@Composable
fun GreenText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Text(text, color = Color(0xFF1AAB9F), fontSize = size, fontWeight = weight, modifier = modifier)
}

@Composable
fun TableFucker(
    text: String,
    color: Color = ColorBlue,
    size: TextUnit = 15.sp,
    weight: FontWeight = FontWeight.Normal,
    customWidth: Dp = 60.dp,
    alignment: Alignment = Alignment.CenterStart
) {
    Box(modifier = Modifier.width(customWidth)) {
        Text(
            text,
            color = color,
            fontSize = size,
            fontWeight = weight,
            modifier = Modifier.align(alignment)
        )
    }
}
