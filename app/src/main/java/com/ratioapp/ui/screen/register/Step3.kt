package com.ratioapp.ui.screen.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.ui.components.MessageErrorTextField
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormRegisterViewModel

@Composable
fun Step3(navController: NavController, onBack: () -> Unit, snackbarHostState: SnackbarHostState) {
    BackHandler {
        onBack()
    }

    val viewModel: FormRegisterViewModel = viewModel()

    Column(
        modifier = Modifier
            .background(Green10)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_create_password),
                contentDescription = null, modifier = Modifier
                    .height(220.dp)
                    .align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Buat Password", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Amankan password kamu dengan password yang jarang digunakan",
                color = Color.Gray, fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column {
                var showPassword by remember {
                    mutableStateOf(false)
                }
                Text(
                    text = "Password", fontFamily = fontFamily,
                )
                Text(
                    text = "Buat password minimal 8 karakter",
                    fontSize = 11.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = viewModel.password,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.password = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Masukkan password kamu",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(
                        '⁕'
                    ),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            if (showPassword)
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eye),
                                    contentDescription = null
                                )
                            else
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeslash),
                                    contentDescription = null
                                )
                        }
                    }
                )
                MessageErrorTextField(message = viewModel.messagesError["password"])
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column {
                var showPassword by remember {
                    mutableStateOf(false)
                }
                Text(
                    text = "Konfirmasi Password", fontFamily = fontFamily,
                )
                Text(
                    text = "Ulangi password yang kamu buat",
                    fontSize = 11.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Masukkan password kamu",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(
                        '⁕'
                    ),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            if (showPassword)
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eye),
                                    contentDescription = null
                                )
                            else
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_eyeslash),
                                    contentDescription = null
                                )
                        }
                    }
                )
                MessageErrorTextField(message = viewModel.messagesError["confirm password"])
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Green10)
                ) {
                    Text(text = "Kembali", fontFamily = fontFamily)
                }
                Button(
                    onClick = {
                              viewModel.createAccount(snackbarHostState =snackbarHostState, navController = navController )
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Green10,
                    ), modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Daftar",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}