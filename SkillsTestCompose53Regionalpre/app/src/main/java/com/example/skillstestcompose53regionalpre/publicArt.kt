package com.example.skillstestcompose53regionalpre

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun PublicArt() {
    val context = LocalContext.current
    var currentPage by remember { mutableStateOf(0) }
    val datas = arrayOf("first", "second")
    val file = context.assets.open("public_art/public_art_${datas[currentPage]}_place.json")
        .bufferedReader()
        .use { it.readText() }
    val gson = Gson()

    data class PublicArt(val title: String, val content: String, val image: String)

    val publicArt = gson.fromJson(file, Array<PublicArt>::class.java).toList()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Title("公共藝術")
            Row {
                Button(
                    onClick = { currentPage = 0 },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(if (currentPage == 0) Color(0xff1aab9f) else Color.White),
                    border = BorderStroke(
                        2.dp, Color(0xff1aab9f)
                    )
                ) {
                    Text("1館", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { currentPage = 1 },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(if (currentPage == 1) Color(0xff1aab9f) else Color.White),
                    border = BorderStroke(
                        2.dp, Color(0xff1aab9f)
                    )
                ) {
                    Text("2館", color = Color.Black)
                }
            }
        }
        Sh()
        LazyColumn(Modifier.padding(horizontal = 20.dp)) {
            items(publicArt) {
                val image =
                    BitmapFactory.decodeStream(context.assets.open("public_art/image/${it.image}"))
                        .asImageBitmap()
                Image(image, contentDescription = "")
                Sh(10.dp)
                Text(it.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(it.content)
                Sh(10.dp)
            }
        }
    }
}