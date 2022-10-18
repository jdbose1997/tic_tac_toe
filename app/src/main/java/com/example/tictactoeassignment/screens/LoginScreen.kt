package com.example.tictactoeassignment.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tictactoeassignment.Constant.TAG
import com.example.tictactoeassignment.navigation.Screen
import com.example.tictactoeassignment.viewmodels.LoginViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavHostController){

    val uiState by viewModel.state.collectAsState()
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = Unit, block = {
        viewModel.navigationUi.collect{
            navController.navigate(Screen.GameRoomScreen.route)
        }
    })

    val currentUser = Firebase.auth.currentUser
    if(currentUser != null){
        viewModel.mobileNumber = currentUser.phoneNumber.toString()
        viewModel.onAction(LoginViewModel.LoginScreenAction.OnLogin)
    }

    Column(Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        when(uiState.userAuthState){
            LoginViewModel.UserAuthState.OTP_SENT ->{
                VerifyMobileNumber(viewModel)
            }
            LoginViewModel.UserAuthState.LOGIN_STATE ->{
                LogInWithMobileNumber(viewModel = viewModel)
            }
            LoginViewModel.UserAuthState.REGISTER_USER_STATE -> {
                registerMobileNumber(activity = activity, viewModel)
            }
            LoginViewModel.UserAuthState.WRONG_OTP -> {

            }
            LoginViewModel.UserAuthState.NEW_REGISTER_STATE -> {
                RegisterMobileNumber(viewModel)
            }
            else -> Unit
        }
    }

}

@Composable
fun LogInWithMobileNumber(viewModel: LoginViewModel){

    var mobileNumberField by remember {
        mutableStateOf("")
    }


    Text(text = "Welcome!")
    Spacer(modifier = Modifier.height(10.dp))
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Please enter your mobile number!", fontSize = 8.sp)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(value = mobileNumberField, onValueChange = {
        mobileNumberField = it
    })
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = {
        viewModel.apply {
            mobileNumber = "+91$mobileNumberField"
        }
        viewModel.onAction(LoginViewModel.LoginScreenAction.OnLogin)
    }) {
        Text(text = "Login")
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Register Now", modifier = Modifier.clickable {
        viewModel.onAction(LoginViewModel.LoginScreenAction.OnNewRegister)
    })
}

@Composable
fun RegisterMobileNumber(viewModel: LoginViewModel){
    var userNameField by remember {
        mutableStateOf("")
    }
    var mobileNumberField by remember {
        mutableStateOf("")
    }


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
        mobileNumberField = "+91$mobileNumberField"
    })
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = {
        viewModel.apply {
            mobileNumber = mobileNumberField
            userName = userNameField
        }
        viewModel.onAction(LoginViewModel.LoginScreenAction.OnRegister)
    }) {
        Text(text = "Register")
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Login Now", modifier = Modifier.clickable {
        viewModel.onAction(LoginViewModel.LoginScreenAction.OnNewLogin)
    })
}

@Composable
fun VerifyMobileNumber(viewModel: LoginViewModel){
    val activity = LocalContext.current as Activity
    var otpField by remember {
        mutableStateOf("")
    }


    Text(text = "Welcome! Please register yourself")
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Please enter your otp!", fontSize = 8.sp)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(value = otpField, onValueChange = {
        otpField = it
    })
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = {
        if(otpField.length == 6){
            val credential = PhoneAuthProvider.getCredential(viewModel.verificationId,otpField)
            signInWithPhoneAuthCredential(activity,credential,viewModel)
        }
    }) {
        Text(text = "Verify")
    }
}


private fun registerMobileNumber(activity: Activity,viewModel: LoginViewModel){
    val options = PhoneAuthOptions.newBuilder(Firebase.auth)
        .setPhoneNumber(viewModel.mobileNumber)       // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(activity)                 // Activity (for callback binding)
        .setCallbacks(object : OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.i(TAG, "onVerificationCompleted: $p0")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.i(TAG, "onVerificationFailed: $p0")
            }

            override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.i(TAG, "onCodeSent: $verificationId")
                viewModel.apply {
                    this.verificationId = verificationId
                    onAction(LoginViewModel.LoginScreenAction.OnOtpSend)
                }
            }

        })          // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun signInWithPhoneAuthCredential(activity: Activity,credential: PhoneAuthCredential,viewModel: LoginViewModel) {
    Firebase.auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "signInWithPhoneAuthCredential: successfull")
                // Sign in success, update UI with the signed-in user's informationz
                viewModel.onAction(LoginViewModel.LoginScreenAction.OnSuccessfulRegister)

                val user = task.result?.user
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {

                }

            }
        }
}


