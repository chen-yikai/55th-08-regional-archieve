package com.example.prepare_notification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.example.prepare_notification.ui.theme.Prepare_notificationTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prepare_notificationTheme {
                Column {
                    ImageFetcher {
                        Image(it, contentDescription = "")
                    }
                    ApiFetcher()
                }
            }
        }
    }
}

@Composable
fun Main() {

}

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

data class UserSignUp(
    val name: String,
    val email: String,
    val password: String
)

@Composable
fun ApiFetcher() {
    val scope = rememberCoroutineScope()
    var data by remember { mutableStateOf<List<User>>(emptyList()) }
    val gson = Gson()
    val client = OkHttpClient()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val request =
                Request.Builder().url("https://skills-auth-api.eliaschen.dev/super").get().build()
            val response = client.newCall(request).execute().use { it.body?.string() }

            data = gson.fromJson(response, object : TypeToken<List<User>>() {}.type)
        }
    }
    Button(onClick = {
        scope.launch {
            withContext(Dispatchers.IO) {
                val postBody = UserSignUp(
                    "elias", "el@fdjdfdsfdffdffdfdffj.fdsf", "1234"
                )
                val jsonBody = gson.toJson(postBody)
                val req = jsonBody.toRequestBody("application/json".toMediaType())
                val request =
                    Request.Builder().url("https://skills-auth-api.eliaschen.dev/signup").post(req)
                        .build()
                client.newCall(request).execute()
            }
        }
    }) {
        Text("New account")
    }
    LazyColumn {
        items(data) {
            Text(it.name)
        }
    }
}

@Composable
fun ImageFetcher(image: @Composable (ImageBitmap) -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                bitmap = URL("https://eliaschen.dev/eliaschen.jpg").openStream().use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (e: Exception) {
                TODO("Not yet implemented")
            }
        }
    }

    bitmap?.let {
        image(it.asImageBitmap())
    }
}