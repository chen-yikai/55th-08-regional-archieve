package com.example.compose_pre_0310

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    @SuppressLint("ServiceCast")
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = manager.getRunningServices(Integer.MAX_VALUE)
        return runningServices.any { it.service.className == serviceClass.name }
    }

    private fun createNotification() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            "media_player",
            "Media PlayBack",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)

        val serviceIntent = Intent(this, MediaPlayerService::class.java)
        if (!isServiceRunning(MediaPlayerService::class.java)) {
            startForegroundService(serviceIntent)
        }
    }
}