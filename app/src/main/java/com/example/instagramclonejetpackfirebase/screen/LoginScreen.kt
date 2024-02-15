package com.example.instagramclonejetpackfirebase.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclonejetpackfirebase.R
import com.example.instagramclonejetpackfirebase.components.CommonProgressSpinner
import com.example.instagramclonejetpackfirebase.navigation.Screen
import com.example.instagramclonejetpackfirebase.navigation.navigateTo
import com.example.instagramclonejetpackfirebase.viewmodel.IgViewModel


@Preview(showBackground = true)
@Composable
fun LoginScreen(navController: NavController, vm: IgViewModel): Unit {
    val usernameState = remember {
        mutableStateOf("")
    }

    val passState = remember {
        mutableStateOf("")
    }

    val focus = LocalFocusManager.current

    Box() {
        if (vm?.inProcess?.value ?: false) {
            CommonProgressSpinner()
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    enabled = !(vm?.inProcess?.value)!!
                ) { }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ig_logo), contentDescription = "logo",
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 16.dp)
            )
            Text(
                text = "Login",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )
            OutlinedTextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

                label = { Text(text = "Username") })

            OutlinedTextField(
                value = passState.value,
                onValueChange = { passState.value = it },
                modifier = Modifier.padding(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(onClick = {
                focus.clearFocus();
//                vm?.onSignup(
//                    usernameState.value,
//
//                    passState.value
//                )

            }) {
                Text(text = "LOGIN")
            }
            Text(
                text = "New here? Go to signup ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, Screen.Signup)
                    }
            )
        }
    }

}