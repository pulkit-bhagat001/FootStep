package com.user.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.user.shoppingapp.presentation.Viewmodel.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel) {
    LaunchedEffect(key1 = Unit) {
        userViewModel.loadProfileDetails()
    }
    val context= LocalContext.current
    val saveDetailsState by userViewModel.saveDetailsState.collectAsState()

    val profileScreen by userViewModel.profileScreen.collectAsState()
    val profileScreenTextField by userViewModel.profileScreenTextField.collectAsState()
    val enabledOrNot = userViewModel.toggle
    Box(Modifier.fillMaxSize()) {
        if (profileScreen.isLoading) {
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
                        "Loading",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

        }else if (profileScreen.error.isNotEmpty()) {
            LaunchedEffect(key1 =profileScreen.error ) {
                Toast.makeText(context, profileScreen.error, Toast.LENGTH_SHORT).show()

            }

        }
        else if(saveDetailsState.isLoading){
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
                        "Updating Details",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
        else if(saveDetailsState.error.isNotEmpty()){
            LaunchedEffect(key1 =saveDetailsState.error ) {
                Toast.makeText(context, saveDetailsState.error, Toast.LENGTH_SHORT).show()

            }
        }
        else if(!saveDetailsState.success.isNullOrEmpty()){
            LaunchedEffect(key1 =saveDetailsState.success ) {
                userViewModel.reset()
                Toast.makeText(context, saveDetailsState.success, Toast.LENGTH_SHORT).show()

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Profile", fontSize = 40.sp, color = Color.Black)
            Spacer(Modifier.height(2.dp))

            CommonTextField(
                text = "First Name",
                onTextChanged = {
                    userViewModel.updateProfileScreenTextField("firstName", it)
                }, value = profileScreenTextField.firstname,
                enabled = enabledOrNot
            )

            Spacer(Modifier.height(4.dp))


            CommonTextField(
                text = "Last Name",
                onTextChanged = {
                    userViewModel.updateProfileScreenTextField("lastName", it)

                },
                modifier = Modifier.height(100.dp), singleLine = false,
                value = profileScreenTextField.lastName,
                enabled = enabledOrNot

            )

            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Email",
                onTextChanged = {
                    userViewModel.updateProfileScreenTextField("email", it)
                }, value = profileScreenTextField.email,
                enabled = enabledOrNot
            )
            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Phone Number",
                onTextChanged = {
                    userViewModel.updateProfileScreenTextField("phoneNumber", it)
                }, value = profileScreenTextField.phoneNumber,
                enabled = enabledOrNot
            )
            Spacer(Modifier.height(4.dp))
            CommonTextField(
                text = "Address",
                onTextChanged = {
                    userViewModel.updateProfileScreenTextField("address", it)
                }, value = profileScreenTextField.address,
                enabled = enabledOrNot
            )
            if (enabledOrNot) {
                Spacer(Modifier.height(30.dp))
                OutlinedButton(
                    onClick = { userViewModel.toggleBool() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF508E54)),

                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text("Edit Profile", color = Color.White , fontSize = 15.sp)
                }
            } else {
                Spacer(Modifier.height(30.dp))
                OutlinedButton(
                    onClick = {
                        userViewModel.toggleBool()
                        userViewModel.saveDetails()

                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF508E54)),
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text("Save Changes", color = Color.White, fontSize = 15.sp)
                }
            }

            OutlinedButton(
                onClick = {
                    userViewModel.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(width = 3.dp, color =  Color(0xFF508E54)),


                ) {
                Text("Logout", color =  Color(0xFF508E54), fontSize = 15.sp)
            }

        }
    }
}