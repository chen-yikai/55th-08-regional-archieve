package com.example.skills55regionalcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skills55regionalcompose.ui.theme.Skills55regionalcomposeTheme
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.
import okhttp3.Dispatcher

data class MusicList(
    val name: String,
    val url: String
)

val client = OkHttpClient()
val gson =

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Skills55regionalcomposeTheme {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {

    Scaffold(topBar = { TopAppBar(title = { Text("聲景探險家") }) }) { padding ->
        val innerPadding = PaddingValues(start = padding.calculateTopPadding(), bottom = 0.dp)
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize()) {

            }
        }
    }
}

suspend fun fetch(url: String) {
    return withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(baseUrl).build()
            val response = client.newCall(request).execute()

            val jsonString = response.body?.string() ?: return@withContext emptyList()

            val listType = object : TypeToken<List<APIrs>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Failed to fetch URL: ${e.message}")
            emptyList()
        }
    }
}
}