package com.example.androidcomposesqlitesuper

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Preview(showBackground = true)
@Composable
fun Home(nav: NavHostController = rememberNavController()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Nav(nav)
    }
}

@Preview
@Composable
fun Nav(nav: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    val dbHandler = DBHandler(context)
    val username = sharedPreferences.getString("name", "User")
    val usermail = sharedPreferences.getString("email", "example@example.com")
    val autoSignin = sharedPreferences.getBoolean("rememberme", false)
    Row(
        modifier = Modifier
            .padding(
                top = WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
            .padding(horizontal = 15.dp)
            .padding(bottom = 20.dp, top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_account_circle_24),
                contentDescription = "Account"
            )
            Wr(7)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$username", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Wr()
                    if (autoSignin) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFf6b2ff))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text("AUTO SIGNIN", fontSize = 10.sp)
                        }
                    }
                }
                Text(text = "$usermail", fontSize = 13.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(R.drawable.baseline_delete_forever_24),
                contentDescription = "Clear All Data",
                modifier = Modifier.clickable {
                    dbHandler.deleteAllData()
                    sharedPreferences.edit().clear().apply()
                    Toast.makeText(context, "All Data Cleared", Toast.LENGTH_SHORT).show()
                    nav.navigate("signup")
                })
            Wr(20)
            Icon(painter = painterResource(R.drawable.baseline_exit_to_app_24),
                contentDescription = "Sign Out",
                modifier = Modifier.clickable {
                    sharedPreferences.edit().clear().apply()
                    Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                    nav.navigate("signin")
                })
        }
    }
}