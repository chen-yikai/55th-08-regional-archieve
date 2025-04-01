package com.example.composealarmprepare

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.composealarmprepare.ui.theme.ComposealarmprepareTheme
import java.time.LocalTime
import java.util.Calendar

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Toast.makeText(this, intent.getStringExtra("message") ?: "none", Toast.LENGTH_SHORT)
                .show()
            ComposealarmprepareTheme {
                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

                var hasPermissions = ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                LaunchedEffect(Unit) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                if (hasPermissions) Text("has permission") else Text("no")

                PrepareTimePicker()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrepareTimePicker() {
    val context = LocalContext.current
    var timePickerState by remember { mutableStateOf(LocalTime.now()) }
    var showPicker by remember { mutableStateOf(false) }

    Column(Modifier.statusBarsPadding()) {
        Text(
            "${
                timePickerState.hour.toString().padStart(2, '0')
            }:${timePickerState.minute.toString().padStart(2, '0')}"
        )
        Button(onClick = {
            showPicker = !showPicker
        }) {
            Text("Open Picker")
        }
        Button(onClick = {
            setExactAlarm(context, 1000, true)
        }) { Text("Cancel Alarm") }
        FilledTonalButton(onClick = {
            setExactAlarm(context, System.currentTimeMillis() + 500)
        }) {
            Text("Send Notify")
        }
        if (showPicker)
            TimePicker(dismiss = { showPicker = false }) { time ->
                timePickerState = time
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(dismiss: () -> Unit, confirm: (LocalTime) -> Unit) {
    var timePickerState = rememberTimePickerState(
        initialMinute = LocalTime.now().minute,
        initialHour = LocalTime.now().hour,
        is24Hour = true
    )
    AlertDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = {
                confirm(LocalTime.of(timePickerState.hour, timePickerState.minute))
                dismiss()
            }) { Text("Ok") }
        },
        dismissButton = {
            TextButton(onClick = dismiss) { Text("Cancel") }
        }, text = {
            androidx.compose.material3.TimePicker(state = timePickerState)
        }
    )
}