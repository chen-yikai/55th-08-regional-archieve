package com.example.skills53dic.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skills53dic.components.ColorBlack
import com.example.skills53dic.components.ColorBlue
import com.example.skills53dic.components.ColorLightGray
import com.example.skills53dic.components.Sh
import com.example.skills53dic.components.Sw
import com.google.gson.Gson

data class PublicArt(val title: String, val content: String, val image: String)

fun getPublicArts(context: Context, place: String): List<PublicArt>? {
    return try {
        val content = context.assets.open("public_art/public_art_${place}.json").bufferedReader()
            .use { it.readText() }
        Gson().fromJson(content, Array<PublicArt>::class.java).toList()
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PublicArt() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var selectedPlace = rememberSaveable { mutableStateOf(1) }
    var places = arrayOf("一館", "二館")
    var places_en = arrayOf("first_place", "second_place")
    var publicArtData: List<PublicArt>? = emptyList()
    var publicArt = publicArtData ?: emptyList()
    publicArtData = getPublicArts(context, places_en[selectedPlace.value - 1])
    publicArt = publicArtData ?: emptyList()

    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp, top = 10.dp)
        ) {
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
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Column {
                publicArt.forEach {
                    Column {
                        Text(
                            it.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorBlack,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        HorizontalDivider()
                        Sh(10.dp)
                        Text(
                            it.content,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = ColorLightGray,
                        )
                        Sh(10.dp)
                        ImageAsset("public_art/image/${it.image}")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageAsset(fileName: String) {
    val context = LocalContext.current
    val file = context.assets.open(fileName)
    val bitmap = BitmapFactory.decodeStream(file)
    Image(
        bitmap = bitmap.asImageBitmap(), contentDescription = fileName
    )
}