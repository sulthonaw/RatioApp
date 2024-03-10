package com.ratioapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ratioapp.R
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.WithDrawalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinScreen(
    navController: NavController,
    viewModel: WithDrawalViewModel,
    snackbarHostState: SnackbarHostState
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_atm_pin))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val contex = LocalContext.current
    val token = AuthStore(contex).getToken.collectAsState(initial = "").value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SANDI",
                        fontFamily = fontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        }, bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Button(
                        onClick = {
                            if (token.isNotBlank()) {
                                viewModel.createWithDrawal(
                                    token = token,
                                    navController,
                                    snackbarHostState
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Bayar",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )
            Text(text = "Masukkan SANDI", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Tolong, masukkan SANDI untuk perlindungan",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { e -> viewModel.password = e },
                modifier = Modifier.width(250.dp),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center, fontFamily = fontFamily),
                visualTransformation = PasswordVisualTransformation('⁕'),
                singleLine = true
            )
        }
    }
}