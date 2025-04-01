package com.example.compose0324player_service_prepare_overself

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotification()
        startForegroundService(Intent(this@App,PlayerService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){
        val manager = this@App.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(player_channel,"Player Channel",NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
    }
}