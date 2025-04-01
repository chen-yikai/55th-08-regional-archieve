package com.example.androidcomposesqlitesuper


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.androidcomposesqlitesuper.Br
import com.example.androidcomposesqlitesuper.DBHandler
import com.example.androidcomposesqlitesuper.GreetInput

@Preview(showBackground = true)
@Composable
fun SignIn(nav: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val dbHandler = DBHandler(context)
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val rememberMe = rememberSaveable { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Br(40)
        GreetInput(
            label = "Email",
            value = email,
        )
        Br()
        GreetInput(
            label = "Password",
            value = password,
        )
        Br()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(checked = rememberMe.value, onCheckedChange = { rememberMe.value = it })
            Text("Remember Me", modifier = Modifier.clickable {
                rememberMe.value = !rememberMe.value
            })
        }
        Br()
        Button(
            onClick = {
                if (dbHandler.authUser(email = email.value, password = password.value)) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().putBoolean("rememberme", rememberMe.value)
                        .putString("email", email.value)
                        .putString("name", dbHandler.getUser(email = email.value)).apply()
                    nav.navigate("home")
                } else {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.shadow(
                elevation = 5.dp, shape = RoundedCornerShape(50.dp)
            )
        ) {
            Text("Sign In")
        }
        Br(40)
        Text("Don't have an account? Sign Up",
            color = Color(0xFF5c6cff),
            modifier = Modifier.clickable {
                nav.navigate("signup")
            })
    }

}

@Preview(showBackground = true)
@Composable
fun SignUp(nav: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val dbHandler = DBHandler(context)
    val email = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Br(40)
        GreetInput(value = email, label = "Email")
        Br()
        GreetInput(value = name, label = "Name")
        Br()
        GreetInput(value = password, label = "Password")
        Br(20)
        Button(
            onClick = {
                dbHandler.addData(email = email.value, name = name.value, password = password.value)
                nav.navigate("signin")
            }, modifier = Modifier.shadow(
                elevation = 5.dp, shape = RoundedCornerShape(50.dp)
            )
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Already have an account? Sign In",
            color = Color(0xFF5c6cff),
            modifier = Modifier.clickable {
                nav.navigate("signin")
            })
    }
}
