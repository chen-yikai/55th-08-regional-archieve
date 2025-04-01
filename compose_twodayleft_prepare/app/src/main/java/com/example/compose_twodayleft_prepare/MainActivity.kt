package com.example.compose_twodayleft_prepare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_twodayleft_prepare.ui.theme.Compose_twodayleft_prepareTheme
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.time.LocalTime
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose_twodayleft_prepareTheme {
                val dbInstance = getDataBase(this@MainActivity)
                val musicListData = MusicListData(dbInstance)
                Main(musicListData)
            }
        }
    }
}

@Composable
fun Main(db: MusicListData = viewModel()) {
    val data by db.data.collectAsState(emptyList())
    var api by remember { mutableStateOf<List<MusicApi>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        api = withContext(Dispatchers.IO) {
            getMusicApi()
        }
        api.forEach {

        }
    }
    Column(Modifier.statusBarsPadding()) {
        Text("Music Data")
        FilledTonalButton(onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val file = File(context.getExternalFilesDir(null), "hello.json")
                    FileWriter(file).use { writer ->
                        writer.write("hello there")
                    }
                }
            }
        }) {
            Text("Write the File")
        }
        FilledTonalButton(onClick = {
            setAlarm(
                context, System.currentTimeMillis()


                        + 5000
            )
        }) {
            Text("set alarm")
        }
        LazyColumn {
            items(api) {
                Text(it.name)
            }
        }
    }
}