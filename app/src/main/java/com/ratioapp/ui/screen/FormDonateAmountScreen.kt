package com.ratioapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.MidtransViewModel
import java.lang.NumberFormatException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDonateAmountScreen(
    navController: NavController,
    photoId: String,
    viewModel: MidtransViewModel
) {

    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Donasi",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                })
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(text = "Donasi", fontFamily = fontFamily, fontSize = 12.sp)
                    Text(
                        text = "Rp. ${
                            if (viewModel.amount.isNotBlank())
                                viewModel.amount.filter {
                                    it.isDigit()
                                } else 0
                        }",
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(text = "Biaya Admin", fontFamily = fontFamily, fontSize = 12.sp)
                    Text(
                        text = "Rp. 1000",
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(text = "Total", fontFamily = fontFamily, fontSize = 12.sp)
                    val filter = viewModel.amount.filter { it.isDigit() }
                    Text(
                        text = "Rp. ${if (filter.isDigitsOnly() && filter.isNotBlank()) filter.toInt() + 1000 else 0}",
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = {
                        viewModel.createDonation(
                            token = token, photoId = photoId, amount =
                            try {
                                viewModel.amount.toInt()
                            } catch (e: NumberFormatException) {
                                0
                            }, navController = navController, context = context
                        )

                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green10)
                ) {
                    if (viewModel.isLoading)
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    else
                        Text(
                            text = "Bayar",
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
        ) {
            Text(text = "Nominal", fontFamily = fontFamily, fontSize = 12.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Green20)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Green30)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Rp.",
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedTextField(
                    value = viewModel.amount,
                    onValueChange = { value ->
                        viewModel.amount = value.take(10)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }

}