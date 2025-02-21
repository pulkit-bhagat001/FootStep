package com.user.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel

@Composable
fun SignUpScreen(userViewModel: UserViewModel, onNavigate: () -> Unit) {
    val context = LocalContext.current
    val signUpTextField by userViewModel.signUpScreenVariables.collectAsState()
    val signUpScreenState by userViewModel.signUpScreen.collectAsState()
    val enabledOrNot by userViewModel.enabledOrNot.collectAsState()
    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "SignUp", fontSize = 40.sp, color = Color.Black)
            Spacer(Modifier.height(2.dp))

            CommonTextField(
                text = "First Name",
                onTextChanged = {
                    userViewModel.updateSignUpScreenTextField("firstName", it)
                }, value = signUpTextField.firstname,
                enabled = enabledOrNot
            )

            Spacer(Modifier.height(4.dp))


            CommonTextField(
                text = "Last Name",
                onTextChanged = {
                    userViewModel.updateSignUpScreenTextField("lastName", it)

                }
              , singleLine = false,
                value = signUpTextField.lastName,
                enabled = enabledOrNot

            )

            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Email",
                onTextChanged = {
                    userViewModel.updateSignUpScreenTextField("email", it)
                }, value = signUpTextField.email,
                enabled = enabledOrNot
            )
            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Password",
                onTextChanged = {
                    userViewModel.updateSignUpScreenTextField("password", it)
                }, value = signUpTextField.password,
                enabled = enabledOrNot
            )
Spacer(Modifier.height(20.dp))
            OutlinedButton(
                onClick = { userViewModel.registerUser() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF508E54)),
                modifier = Modifier.fillMaxWidth(),
                enabled = !enabledOrNot

            ) {
                Text("Submit", color = Color.White, fontSize = 15.sp)
            }

        }
        if (signUpScreenState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f)), contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.Black)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Registering User",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

        } else if (signUpScreenState.error.isNotEmpty()) {
            Toast.makeText(context, signUpScreenState.error, Toast.LENGTH_SHORT).show()
            userViewModel.clearRegisterState()

        }
        else if (!signUpScreenState.success.isNullOrEmpty()) {
            //Toast.makeText(context, signUpScreenState.success, Toast.LENGTH_SHORT).show()
            //userViewModel.clearRegisterState()
           // onNavigate()
            LaunchedEffect(key1 = signUpScreenState.success) {
                Toast.makeText(context, signUpScreenState.success, Toast.LENGTH_SHORT).show()
                onNavigate()
            }


        }
    }
}


@Composable
fun CommonTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    value: String,
    isError: Boolean = false,
    enabled: Boolean
) {

    OutlinedTextField(value = value, onValueChange = {
        onTextChanged(it)
    }, label = { Text(text = text) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(),
        isError = isError,//!isError,
        readOnly = enabled


    )

}