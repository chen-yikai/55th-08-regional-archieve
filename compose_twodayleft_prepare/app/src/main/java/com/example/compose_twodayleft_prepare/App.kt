package com.example.compose_twodayleft_prepare

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

const val alarm_class = "alarm_channel"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmChannel =
            NotificationChannel(alarm_class, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(alarmChannel)
    }

}