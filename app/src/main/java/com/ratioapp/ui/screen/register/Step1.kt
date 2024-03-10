package com.ratioapp.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.components.MessageErrorTextField
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1(navController: NavController, onNext: () -> Unit) {
    val viewModel: FormRegisterViewModel = viewModel()

    Column(
        modifier = Modifier
            .background(Green10)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(30.dp)) {
            Image(
                painter = painterResource(id = R.drawable.bg_register),
                contentDescription = null,
                modifier = Modifier.height(220.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Daftar Akun",
                fontFamily = fontFamily,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Daftar! Lalu jelajah ratio dan tambahkan hasil menarikmu di ratio",
                fontFamily = fontFamily, fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Email", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = viewModel.email,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.email = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    placeholder = {
                        Text(
                            text = "Masukkan email kamu",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    })
                MessageErrorTextField(message = viewModel.messagesError["email"])
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Username", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = viewModel.username,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.username = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Masukkan username kamu",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    })
                MessageErrorTextField(message = viewModel.messagesError["username"])
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNext, colors = ButtonDefaults.buttonColors(
                        containerColor = Green10,
                    ), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Lanjut daftar",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold
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
                        text = "Sudah punya akun?",
                        fontFamily = fontFamily,
                        fontSize = fontSize
                    )
                    Text(text = "Masuk",
                        fontFamily = fontFamily,
                        fontSize = fontSize,
                        color = Green10,
                        textDecoration = TextDecoration.Underline,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate(NavigationItem.Login.route)
                        })
                }
            }
        }
    }
}