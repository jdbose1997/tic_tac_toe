package com.example.tictactoeassignment.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoeassignment.viewModels.LoginViewModel

@Composable
fun LoginScreen(viewModel : LoginViewModel){

    var userNameField by remember {
        mutableStateOf("")
    }
    var mobileNumberField by remember {
      mutableStateOf("")
    }
    Column(Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome! Please register yourself")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Please enter your name!", fontSize = 8.sp)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(value = userNameField, onValueChange = {
            userNameField = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Please enter your mobile number!", fontSize = 8.sp)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(value = mobileNumberField, onValueChange = {
            mobileNumberField = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            viewModel.onAction(
                LoginViewModel.LoginScreenAction.OnLogin(
                    mobileNumber = mobileNumberField,
                    userName = userNameField
                )
            )
        }) {
            Text(text = "Login")
        }
    }
}


@Preview
@Composable
fun preview(){
    LoginScreen(LoginViewModel())
}