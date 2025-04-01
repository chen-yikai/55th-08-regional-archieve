package com.example.composemediasessionpreparetest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundService(Intent(this, PlaybackService::class.java))
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "media_channel",
            "Media Playback",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Channel for media playback notifications"
        }
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }
}