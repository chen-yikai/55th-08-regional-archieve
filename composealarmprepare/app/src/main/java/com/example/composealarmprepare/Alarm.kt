package com.example.composealarmprepare

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.BufferUnderflowException


@RequiresApi(Build.VERSION_CODES.O)
class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onReceive(context: Context, intent: Intent) {
        sendNotification(context, intent)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun sendNotification(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent =
            Intent(context, MainActivity::class.java).putExtra("message", "hello from alarm")
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification =
            NotificationCompat.Builder(context, alarm_channel)
                .setContentTitle("Hello")
                .setContentText("Hello World")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

        manager.notify(1, notification)
        setExactAlarm(context, intent.getLongExtra("time", AlarmManager.INTERVAL_DAY))
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("ScheduleExactAlarm")
fun setExactAlarm(context: Context, triggerTime: Long, cancel: Boolean = false) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("time", triggerTime)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (!cancel) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    } else {
        alarmManager.cancel(pendingIntent)
    }
}