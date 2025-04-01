package com.example.composenotifypush

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.composenotifypush.ui.theme.ComposenotifypushTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val channelId = "example_channel"
    private val notificationId = 1

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposenotifypushTheme {
                val context = LocalContext.current
                val postNotify = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                val notifyService = NotifyService(this)
                LaunchedEffect(true) {
                    if (!postNotify.status.isGranted) {
                        postNotify.launchPermissionRequest()
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    Column {

                        Button(
                            onClick = {
                                notifyService.showNotfication()
                            }, modifier = Modifier.padding(paddingValues)
                        ) {
                            Text(text = "Send Notification")
                        }
                        Button(onClick = { scheduleWorker(context) }) { Text("Start WorkManager") }
                    }
                }
            }
        }
    }
}

fun scheduleWorker(context: Context) {
    val workRequest: WorkRequest = OneTimeWorkRequestBuilder<Worker>()
        .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}