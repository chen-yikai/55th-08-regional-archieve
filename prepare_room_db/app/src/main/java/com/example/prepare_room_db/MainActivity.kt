package com.example.prepare_room_db

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Contacts.Intents.UI
import android.util.Log
import android.view.inputmethod.InputContentInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prepare_room_db.ui.theme.Prepare_room_dbTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prepare_room_dbTheme {
                val database = getRoom(this)
                val todoDb = TodoViewModel(database)
                Column(
                    Modifier
                        .statusBarsPadding()
                        .imePadding()
                ) {
                    TodoList(todoDb)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(state: Boolean, title: String, onChange: () -> Unit, content: @Composable () -> Unit) {
    if (state) {
        ModalBottomSheet(onDismissRequest = onChange) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))
                content()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoList(db: TodoViewModel = viewModel()) {
    val todos by db.allTodo.collectAsState(emptyList())
    var showAdd by remember { mutableStateOf(false) }
    var showUpdate by remember { mutableStateOf(false) }
    var textFiledValue by remember { mutableStateOf("") }
    var clickItem by remember { mutableStateOf(0) }

    Dialog(showUpdate, "Update Todo", { showUpdate = !showUpdate }) {
        OutlinedTextField(
            value = textFiledValue,
            onValueChange = { textFiledValue = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            db.updateName(textFiledValue, clickItem)
            showUpdate = false
        }, Modifier.fillMaxWidth()) {
            Text("Update")
        }
    }

    Dialog(showAdd, "Add Todo", { showAdd = !showAdd }) {
        OutlinedTextField(
            value = textFiledValue,
            onValueChange = { textFiledValue = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            db.insert(
                Todo(
                    todo = textFiledValue, done = false
                )
            )
            textFiledValue = ""
            showAdd = false
        }, modifier = Modifier.fillMaxWidth()) { Text("Add") }
    }

    Scaffold(Modifier.fillMaxSize(), floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            showAdd = !showAdd
        }, icon = {
            Icon(
                Icons.Default.Add, contentDescription = ""
            )
        }, text = { Text("Add Todo") })
    }) { _ ->
        LazyColumn(
            Modifier
                .statusBarsPadding()
                .padding(10.dp)
        ) {
            item {
                Text("Your Todo", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            }
            if (todos.isNotEmpty())
                items(todos) {
                    var checked by remember { mutableStateOf(false) }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                showUpdate = !showUpdate
                                clickItem = it.id
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = it.done, onCheckedChange = { checkState ->
                                checked = checkState
                                db.updateDone(checked, it.id)
                            })
                            Text(it.todo)
                        }
                        IconButton(onClick = {
                            db.deleteTodo(it.id)
                        }) { Icon(Icons.Default.Delete, contentDescription = "") }
                    }
                    HorizontalDivider()
                }
        }
        if (todos.isEmpty())
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Nothing to show",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
    }
}