package com.example.compose0325test

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotification()
        startForegroundService(Intent(this@App, MediaService::class.java))
        CoroutineScope(Dispatchers.Main).launch {
            Widget().updateAll(this@App)
        }
    }

    private fun createNotification() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mediaChannel =
            NotificationChannel(media_channel, "Media Channel", NotificationManager.IMPORTANCE_LOW)
        val alarmChannel =
            NotificationChannel(alarm_channel, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannels(listOf(mediaChannel, alarmChannel))
    }
}