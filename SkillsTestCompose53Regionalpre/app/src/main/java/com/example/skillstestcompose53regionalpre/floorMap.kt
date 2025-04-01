package com.example.skillstestcompose53regionalpre

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun FloorMap() {
    var currentFloor by remember { mutableStateOf(0) }
    var floors = arrayOf(R.drawable.floor_3d_first_place, R.drawable.floor_3d_second_place)
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Title("樓層立體圖")
            Row {
                Button(
                    onClick = { currentFloor = 0 },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(if (currentFloor == 0) Color(0xff1aab9f) else Color.White),
                    border = BorderStroke(
                        2.dp, Color(0xff1aab9f)
                    )
                ) {
                    Text("1館", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { currentFloor = 1 },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(if (currentFloor == 1) Color(0xff1aab9f) else Color.White),
                    border = BorderStroke(
                        2.dp, Color(0xff1aab9f)
                    )
                ) {
                    Text("2館", color = Color.Black)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(floors[currentFloor]),
            modifier = Modifier.fillMaxSize(),
            contentDescription = ""
        )
    }
}