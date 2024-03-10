package com.ratioapp.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.ViewUiState
import com.ratioapp.viewModels.MidtransViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MidtransScreen(navController: NavController, viewModel: MidtransViewModel) {
//    val viewModel: MidtransViewModel = viewModel()
//    val test = viewModel.paymentUiState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Midtrans Payment") }, navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }) {
        Column(modifier = Modifier.padding(it)) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                WebView(context).apply {
                    this.settings.javaScriptEnabled = true
                    this.settings.javaScriptCanOpenWindowsAutomatically = true
                    this.settings.domStorageEnabled = true
                    this.settings.databaseEnabled = true
                    this.settings.allowFileAccessFromFileURLs = true
                    this.settings.allowFileAccess = true
                    this.settings.allowContentAccess = true
                    this.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    when (viewModel.paymentUiState) {
                        is ViewUiState.Loading -> {}
                        is ViewUiState.Success -> {
                            val data = viewModel.paymentUiState as ViewUiState.Success
                            loadUrl(data.data.redirectUrl)
                        }

                        is ViewUiState.Error -> {}
                    }
                }
            })
        }
    }
}