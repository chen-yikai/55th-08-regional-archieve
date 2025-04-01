package com.example.composenotifypush

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotifyService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showNotfication() {
        val notification = NotificationCompat.Builder(context, "reflex_channel")
            .setContentTitle("Water Reminder")
            .setContentText("Time to drink some water!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}