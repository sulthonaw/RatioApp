package com.ratioapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.components.MessageErrorTextField
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: FormLoginViewModel = viewModel()
    val context = LocalContext.current
    val snackbarHostState by mutableStateOf(
        SnackbarHostState()
    )
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null, modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Welcome Back,\nRationers! 👋",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold, fontSize = 26.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Masukkan info akunmu, untuk masuk ke ratio",
                    fontFamily = fontFamily,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(27.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = "Email", fontFamily = fontFamily)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = viewModel.email,
                        textStyle = TextStyle(fontFamily = fontFamily),
                        onValueChange = { viewModel.email = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        placeholder = {
                            Text(
                                text = "Masukkan email kamu",
                                fontFamily = fontFamily,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                    )
                    MessageErrorTextField(
                        message = viewModel.messagesError["email"]
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    var showPassword by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_key),
                            contentDescription = null,
                            tint = Green10,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = "Password", fontFamily = fontFamily)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = viewModel.password,
                        textStyle = TextStyle(fontFamily = fontFamily),
                        onValueChange = { viewModel.password = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
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
                    MessageErrorTextField(
                        message = viewModel.messagesError["password"]
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            viewModel.login(
                                context = context,
                                navController = navController, snackbarHostState
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green10,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (viewModel.uiState == ViewUiState.Loading<Nothing>(isLoading = false))
                            Text(
                                text = "Login",
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                        else
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White, strokeWidth = 2.dp
                            )
                    }
                    Spacer(
                        modifier = Modifier
                            .border(2.dp, Green10, RoundedCornerShape(10.dp))
                            .width(100.dp)
                            .height(1.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        val fontSize = 14.sp
                        Text(
                            text = "Tidak punya akun?",
                            fontFamily = fontFamily,
                            fontSize = fontSize
                        )
                        Text(text = "Daftar",
                            fontFamily = fontFamily,
                            fontSize = fontSize,
                            color = Green10,
                            textDecoration = TextDecoration.Underline,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate(NavigationItem.Registration.route)
                            })
                    }
                }
            }
        }
    }
}