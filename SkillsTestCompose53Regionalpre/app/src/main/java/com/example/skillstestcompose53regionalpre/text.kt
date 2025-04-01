package com.example.skillstestcompose53regionalpre

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun test(){
    Text("hello", fontSize = 100.sp)
    Box (Modifier.height(10.dp)){  }
}