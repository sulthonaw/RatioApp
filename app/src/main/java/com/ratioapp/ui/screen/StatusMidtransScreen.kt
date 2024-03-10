package com.ratioapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.models.Status
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.viewModels.StatusMidtransViewModel

@Composable
fun StatusMidtransScreen(navController: NavController) {
    val viewModel: StatusMidtransViewModel = viewModel()

    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val donationId = AuthStore(context).getDonationId.collectAsState(initial = "").value

    if (donationId.isNotBlank() && token.isNotBlank()) {
        viewModel.getStatus(donationId, token)
    }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Button(
                    onClick = { navController.navigate(NavigationItem.Wallet.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = viewModel.donationStatus == Status.SUCCESS
                ) {
                    Text(text = "Lanjut")
                }
            }
        }
    ) {
        PullRefresh(onRefresh = {
            if (donationId.isNotBlank() && token.isNotBlank()) {
                viewModel.getStatus(donationId, token)
            }
        }, refreshing = viewModel.isLoading) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    if (!viewModel.isLoading)
                        when (viewModel.donationStatus) {
                            Status.PENDING -> {
                                Text(text = "Pending")
                            }

                            Status.SUCCESS -> {
                                Text(text = "Success")
                            }

                            Status.FAILED -> {
                                Text(text = "Pending")
                                Text(
                                    text = "Refresh jika, pembayaran terselesaikan",
                                    fontSize = 12.sp
                                )

                            }
                        }
                    else
                        CircularProgressIndicator()
                }
            }
        }
    }
}