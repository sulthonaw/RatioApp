package com.ratioapp.ui.screen.register

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.ui.theme.Green10
import com.ratioapp.viewModels.FormRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController) {
    val viewModel: FormRegisterViewModel = viewModel()
    val snackbarHostState by mutableStateOf(
        SnackbarHostState()
    )
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .background(Green10)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .height(3.dp)
                        .border(
                            3.dp,
                            if (viewModel.stateScreen == (it + 1)) Color.White else Color.LightGray
                        )
                )
            }
        }
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            when (viewModel.stateScreen) {
                1 -> {
                    Step1(navController = navController) {
                        viewModel.stateScreen = 2
                    }
                }

                2 -> {
                    Step2(navController = navController, onBack = {
                        viewModel.stateScreen = 1
                    }, onNext = { viewModel.stateScreen = 3 })
                }


                3 -> {
                    Step3(navController = navController, onBack = {
                        viewModel.stateScreen = 2
                    }, snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

