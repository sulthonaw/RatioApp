package com.ratioapp.ui.screen

import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.libs.whenUiState
import com.ratioapp.models.ViewUiState
import com.ratioapp.models.Wallet
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.BottomAppBar
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.WalletViewModel
import java.text.SimpleDateFormat
import java.util.Locale

enum class CateegoryWalletScreen {
    Masuk, Keluar
}

@Composable
fun WalletScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    val viewModel: WalletViewModel = viewModel()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value

    if (token.isNotBlank()) {
        viewModel.getWallet(token)
    }

    var screen by remember {
        mutableStateOf(CateegoryWalletScreen.Keluar)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(navController = navController, currentRoute = NavigationItem.Wallet.route)
        }, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        PullRefresh(
            onRefresh = {
                if (token.isNotBlank()) {
                    viewModel.getWallet(token, true)
                }
            },
            refreshing = viewModel.uiState == ViewUiState.Loading<Wallet>(
                isLoading = true,
                withPullRefresh = true
            )
        ) {
            Column(
                modifier = Modifier
                    .background(Green30.copy(0.2f))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Green10)
                        .padding(horizontal = 12.dp, vertical = 18.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Dompet Donasi",
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                    whenUiState(uiState = viewModel.uiState, onSuccess = {
                        val data = viewModel.uiState as ViewUiState.Success
                        Text(
                            text = "Rp. ${data.data.amount}",
                            fontFamily = fontFamily,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 6.dp)
                        )
                    }, onError = {
                        Text(
                            text = "Rp. Error",
                            fontFamily = fontFamily,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 6.dp)
                        )
                    }, onLoading = {
                        Text(
                            text = "Rp. Wait..",
                            fontFamily = fontFamily,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 6.dp)
                        )
                    })

                    Button(
                        onClick = {
                            navController.navigate(NavigationItem.WithDrawal.route)
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Green10
                        )
                    ) {
                        Text(
                            text = "Cairkan",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(26.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { screen = CateegoryWalletScreen.Masuk },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (screen == CateegoryWalletScreen.Masuk) Green10 else Color.White,
                            contentColor = if (screen == CateegoryWalletScreen.Masuk) Color.White else Green10
                        ),
                        border = BorderStroke(2.dp, Green10.copy(0.2f))
                    ) {
                        Text(text = "Masuk", fontFamily = fontFamily)
                    }
                    Button(
                        onClick = { screen = CateegoryWalletScreen.Keluar },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (screen == CateegoryWalletScreen.Keluar) Green10 else Color.White,
                            contentColor = if (screen == CateegoryWalletScreen.Keluar) Color.White else Green10
                        ), border = BorderStroke(2.dp, Green10.copy(0.2f))
                    ) {
                        Text(text = "Keluar", fontFamily = fontFamily)
                    }

                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        when (screen) {
                            CateegoryWalletScreen.Masuk -> {
                                whenUiState(
                                    uiState = viewModel.uiState,
                                    onLoading = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(30.dp))
                                        }
                                    },
                                    onError = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = "Error")
                                        }
                                    }) {
                                    val data = viewModel.uiState as ViewUiState.Success
                                    data.data.donation.map {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    Color.LightGray.copy(0.3f),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clip(
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .background(Color.White)
                                                .padding(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(
                                                        RoundedCornerShape(10.dp)
                                                    )
                                                    .background(Green30)
                                                    .padding(6.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_money),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(34.dp)
                                                )
                                            }
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                val date = SimpleDateFormat(
                                                    "dd - MMMM - yyyy",
                                                    Locale.getDefault()
                                                )
                                                Column {
                                                    Text(
                                                        text = "Rp. ${it.amount}",
                                                        fontFamily = fontFamily,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 18.sp
                                                    )
                                                    Text(
                                                        text = "${date.format(it.createdAt)}",
                                                        fontFamily = fontFamily,
                                                        fontSize = 10.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                                Row {
                                                    Text(
                                                        text = "by ${it.user.username}",
                                                        fontFamily = fontFamily,
                                                        fontSize = 10.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (data.data.donation.isEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    1.dp,
                                                    Color.LightGray.copy(0.3f),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clip(
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .background(Color.White)
                                                .padding(12.dp)
                                        ) {
                                            Text(text = "Tidak ada dompet masuk", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }

                            CateegoryWalletScreen.Keluar -> {
                                whenUiState(
                                    uiState = viewModel.uiState,
                                    onLoading = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(30.dp))
                                        }
                                    },
                                    onError = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = "Error")
                                        }
                                    }) {
                                    val data = viewModel.uiState as ViewUiState.Success
                                    data.data.withdrawals.map {
                                        val date = SimpleDateFormat(
                                            "dd - MMMM - yyyy",
                                            Locale.getDefault()
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    Color.LightGray.copy(0.3f),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clip(
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .background(Color.White)
                                                .padding(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(
                                                        RoundedCornerShape(10.dp)
                                                    )
                                                    .background(Color.Red.copy(0.1f))
                                                    .padding(6.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_money),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(34.dp),
                                                    tint = Color.Red
                                                )
                                            }
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "- Rp. ${it.amount}",
                                                        fontFamily = fontFamily,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 18.sp
                                                    )
                                                    Text(
                                                        text = date.format(it.createdAt),
                                                        fontFamily = fontFamily,
                                                        fontSize = 10.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (data.data.withdrawals.isEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    1.dp,
                                                    Color.LightGray.copy(0.3f),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clip(
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .background(Color.White)
                                                .padding(12.dp)
                                        ) {
                                            Text(text = "Tidak ada dompet keluar", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}