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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.PlatformTextStyle
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
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormRegisterViewModel

@Composable
fun Step2(navController: NavController, onBack: () -> Unit, onNext: () -> Unit) {
    val viewModel: FormRegisterViewModel = viewModel()
    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .background(Green10)
    ) {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_register_2),
                contentDescription = null,
                modifier = Modifier
                    .height(220.dp)
            )
        }
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Data diri",
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
            Text(text = "Tambahkan detail data diri kamu", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Column {
                    Text(text = "Nama Lengkap", fontFamily = fontFamily)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = viewModel.fullName,
                        onValueChange = { viewModel.fullName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 1,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Masukkan nama lengkap kamu",
                                fontFamily = fontFamily,
                                fontSize = 14.sp, color = Color.Gray
                            )
                        },
                    )
                    MessageErrorTextField(message = viewModel.messagesError["fullName"])
                }
                Column {
                    Text(text = "Alamat", fontFamily = fontFamily)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = viewModel.address,
                        onValueChange = { viewModel.address = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3,
                        placeholder = {
                            Text(
                                text = "Masukkan alamat kamu",
                                fontFamily = fontFamily,
                                fontSize = 14.sp, color = Color.Gray
                            )
                        },
                    )
                    MessageErrorTextField(message = viewModel.messagesError["addres"])
                }
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
                        onClick = onNext, colors = ButtonDefaults.buttonColors(
                            containerColor = Green10,
                        ), modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Lanjut",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}