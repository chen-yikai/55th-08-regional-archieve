package com.example.compose_twodayleft_prepare

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationManager =
            NotificationCompat.Builder(context, alarm_class).setContentTitle("Time to listen music")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("tap to play").build()
        manager.notify((1..100).random(), notificationManager)
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setAlarm(context: Context, time: Long) {
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
}