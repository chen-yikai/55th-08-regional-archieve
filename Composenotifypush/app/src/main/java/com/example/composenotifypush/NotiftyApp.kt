package com.example.composenotifypush

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class NotiftyApp : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val notifyChannel = NotificationChannel(
            "reflex_channel",
            "Relaxer",
            NotificationManager.IMPORTANCE_HIGH
        )
        notifyChannel.description = "A relax notification"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifyChannel)
    }
}

class Worker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.Main).launch {
            executeFunction()
        }
        return Result.success()
    }

    private suspend fun executeFunction() {
        val service = NotifyService(applicationContext)
        while (true) {
            service.showNotfication()
        }
    }
}
