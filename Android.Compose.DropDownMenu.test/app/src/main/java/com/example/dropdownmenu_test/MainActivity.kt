package com.example.dropdownmenu_test

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}


@Composable
fun hello(name: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "hello $name",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Red)
        )
        Text(text = "I love $name", fontSize = 20.sp)
    }
}

@Composable
fun dropdown() {
    val context = LocalContext.current
    val focusRequester = FocusRequester()
    var expand by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }
    var labels by remember { mutableStateOf(arrayListOf<String>("Label 1", "Label 2", "Label 3")) }
//    var selected by remember { mutableStateOf("") }

//    LaunchedEffect(selected) {
//        Toast.makeText(context, "\"$selected\" was selected", Toast.LENGTH_SHORT).show()
//    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (labels.size != 0) {
            Row {
                DropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                    for (label in labels) {
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            Toast.makeText(
                                context, "\"$label\" was selected", Toast.LENGTH_SHORT
                            ).show()
                            expand = false
                        })
                    }
                }
                Button(onClick = { expand = !expand }) {
                    Text(text = "Toggle")
                }
            }
        } else {
            Text(
                text = "No labels",
                fontSize = 30.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(value = input,
                onValueChange = { input = it },
                singleLine = true,
                placeholder = { Text(text = "Add Label to the DropDown") })
            Button(onClick = {
                if (input.trim().isNotEmpty()) {
                    labels.add(input)
                } else {
                    Toast.makeText(context, "Empty Input is not acceptable", Toast.LENGTH_SHORT)
                        .show()
                }
                input = ""
            }, modifier = Modifier.padding(10.dp)) {
                Text(text = "Add")
            }
        }
        Spacer(modifier = Modifier.size(50.dp))
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color.Black)
                    .verticalScroll(rememberScrollState())
                    .padding(15.dp),
            ) {
                labels.forEachIndexed { index, label ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "$label", fontSize = 25.sp)
                        Spacer(Modifier.weight(1f))
                        Button(onClick = {
                            labels = labels.filterIndexed { i, _ -> i != index }.toMutableList() as ArrayList<String>
                        }) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Main() {
//    hello("elias")
    dropdown()
}
