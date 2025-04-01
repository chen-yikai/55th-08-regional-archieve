package com.example.androidcomposeapilocalsimple

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidcomposeapilocalsimple.ui.theme.AndroidComposeAPIlocalsimpleTheme
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainView(context = this)
        }
    }
}

fun loadCitiesFromAsset(context: Context): CityList? {
    return try {
        val inputStream = context.assets.open("data.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val gson = Gson()
        gson.fromJson(reader, CityList::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

data class City(val name: String, val population: Int)
data class CityList(val cities: List<City>)

@Composable
fun MainView(context: Context) {
    val cityList = remember { loadCitiesFromAsset(context) }
    val cities = cityList?.cities ?: emptyList()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ForEach Method")
        cityList?.cities?.forEach { city ->
            Text(text = "${city.name} - Population: ${city.population}", fontSize = 20.sp)
        } ?: Text(text = "No cities found.")
        Spacer(modifier = Modifier.height(20.dp))
        Text("LazyColumn Method")
        LazyColumn {
            items(cities) { item ->
                Text(text = item.name, fontSize = 20.sp)
            }
        }
    }

}