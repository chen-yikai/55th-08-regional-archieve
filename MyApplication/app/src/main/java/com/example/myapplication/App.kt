package com.example.myapplication

import android.app.Application
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class App : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        UpdateWidget()
    }

    fun UpdateWidget() {
        appScope.launch {
            BarWidget().updateAll(applicationContext)
        }
    }
}