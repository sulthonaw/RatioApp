package com.ratioapp.ui.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.libs.whenUiState
import com.ratioapp.models.ViewUiState
import com.ratioapp.models.Wallet
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.WithDrawalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormWithDrawal(navController: NavController, viewModel: WithDrawalViewModel = viewModel()) {
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value

    if (token.isNotBlank()) {
        viewModel.getWallet(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Penarikan",
                    fontFamily = fontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Button(
                        onClick = { navController.navigate(NavigationItem.Pin.route) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        enabled = if ((viewModel.uiState is ViewUiState.Success)) {
                            (viewModel.uiState as ViewUiState.Success).data.amount != 0 && viewModel.amount.isNotBlank() && ((viewModel.uiState as ViewUiState.Success).data.amount > viewModel.amount.toInt())
                        } else false
                    ) {
                        Text(
                            text = "Tarik",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) {
        PullRefresh(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp), onRefresh = {
                if (token.isNotBlank()) {
                    viewModel.getWallet(token, true)
                }
            }, refreshing = viewModel.uiState == ViewUiState.Loading<Wallet>(isLoading = true, true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE4F7F0))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Saldo",
                            fontFamily = fontFamily,
                            fontSize = 12.sp,
                            lineHeight = 1.sp
                        )
                        whenUiState(
                            uiState = viewModel.uiState,
                            onLoading = {
                                Text(
                                    text = "Rp. Wait..",
                                    fontFamily = fontFamily,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            },
                            onError = {
                                Text(
                                    text = "Rp. Error",
                                    fontFamily = fontFamily,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }) {
                            val data = viewModel.uiState as ViewUiState.Success
                            Text(
                                text = "Rp. ${data.data.amount}",
                                fontFamily = fontFamily,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Nominal", fontFamily = fontFamily, fontSize = 12.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
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
                        onValueChange = { value -> viewModel.amount = value },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Green30,
                            focusedBorderColor = Green20
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}