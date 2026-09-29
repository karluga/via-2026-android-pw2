package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "1-st Activity",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(context, SecondActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Go to 2-nd")
            }

            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Dialog")
            }
        }

        if (showDialog) {
            GroupDialog(
                onDismissRequest = {
                    showDialog = false
                },
                onCloseClicked = {
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun GroupDialog(
    onDismissRequest: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val context = LocalContext.current
    var currentToast by remember { mutableStateOf<Toast?>(null) }

    fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
            show()
        }
    }

    val groupMembers = remember {
        listOf("Kārlis Ivars Braķis", "Andrejs Ņesterovičs")
    }
    val checkedStates = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(groupMembers.size) { add(false) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "2 Group's Dialog")
        },
        text = {
            Column {
                groupMembers.forEachIndexed { index, memberName ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val newState = !checkedStates[index]
                                checkedStates[index] = newState
                                val toastMsg = if (newState) {
                                    "$memberName checked"
                                } else {
                                    "$memberName unchecked"
                                }
                                showToast(toastMsg)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = memberName)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onDismissRequest()
                        val intent = Intent(context, SecondActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Go to 2-nd")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showToast("You clicked OK")
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showToast("You closed dialog")
                    onCloseClicked()
                }
            ) {
                Text("Close")
            }
        }
    )
}

// This allows to see layout in split-screen editor
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyApplicationTheme() {
        SecondScreen()
    }
}