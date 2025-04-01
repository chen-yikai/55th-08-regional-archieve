package com.example.skills53dic.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skills53dic.components.Sh
import com.example.skills53dic.R
import com.example.skills53dic.components.ColorBlack
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.Sw

@Preview(showBackground = true)
@Composable
fun Floor3d() {
    var selectedPlace = rememberSaveable { mutableStateOf(1) }
    var places = arrayOf("一館", "二館")
    var placesImage = arrayOf(R.drawable.floor_3d_first_place, R.drawable.floor_3d_second_place)
    Column {
        Sh(20.dp)
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            places.forEachIndexed { index, item ->
                Button(
                    onClick = {
                        selectedPlace.value = index + 1
                    }, shape = RoundedCornerShape(0.dp), colors = ButtonDefaults.buttonColors(
                        if (index + 1 == selectedPlace.value) ColorBlue else Color.Transparent
                    ), modifier = Modifier
                        .background(
                            if (index + 1 == selectedPlace.value) ColorBlue else Color.Transparent
                        )
                        .border(1.dp, ColorBlack)
                ) {
                    Text(
                        item,
                        color = if (index + 1 == selectedPlace.value) Color.White else ColorBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Sw(10.dp)
            }
        }
        Sh(20.dp)
        Image(
            painter = painterResource(id = if (selectedPlace.value == 1) placesImage[0] else placesImage[1]),
            contentDescription = "The ${if (selectedPlace.value == 1) "first" else "second"} place 3d floor guide",
            modifier = Modifier.fillMaxSize()
        )

    }
}