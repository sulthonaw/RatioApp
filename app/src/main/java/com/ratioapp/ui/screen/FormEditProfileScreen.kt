package com.ratioapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormEditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditProfileScreen(navController: NavController) {
    val viewModel: FormEditProfileViewModel = viewModel()
    val context = LocalContext.current
    val userId = AuthStore(context).getUserId.collectAsState(initial = "").value
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val snackbarHostState by mutableStateOf(
        SnackbarHostState()
    )

    if (token.isNotBlank() && userId.isNotBlank()) {
        viewModel.getUser(token = token, userId = userId, snackbarHostState = snackbarHostState)
    }


    Scaffold(
        topBar = {
            Surface(shadowElevation = 1.dp) {
                TopAppBar(title = {
                    Text(
                        text = "Edit Profile",
                        fontFamily = fontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { it ->
        PullRefresh(
            modifier = Modifier
                .padding(it),
            onRefresh = {
                if (token.isNotBlank() && userId.isNotBlank()) {
                    viewModel.getUser(
                        token = token,
                        userId = userId,
                        snackbarHostState = snackbarHostState,
                        withPullRefresh = true
                    )
                }
            },
            refreshing = (viewModel.uiState == ViewUiState.Loading<Photo>(
                isLoading = true,
                withPullRefresh = true
            ))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (viewModel.uiState) {
                    is ViewUiState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ViewUiState.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(
                                            CircleShape
                                        )
                                        .border(
                                            1.dp, Green20, CircleShape
                                        )
                                        .size(100.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Green20)
                                        .padding(8.dp)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_pencil_square),
                                        contentDescription = null,
                                        Modifier.size(14.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Column(Modifier.fillMaxWidth()) {
                                Text(text = "Email")
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = viewModel.email,
                                    onValueChange = { value -> viewModel.email = value },
                                    textStyle = TextStyle(fontFamily = fontFamily),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    enabled = false
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Nama Lengkap")
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = viewModel.fullName,
                                    onValueChange = { value -> viewModel.fullName = value },
                                    textStyle = TextStyle(fontFamily = fontFamily),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Username")
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = viewModel.username,
                                    onValueChange = { value -> viewModel.username = value },
                                    textStyle = TextStyle(fontFamily = fontFamily),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Alamat")
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = viewModel.address,
                                    onValueChange = { value -> viewModel.address = value },
                                    textStyle = TextStyle(fontFamily = fontFamily),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        if (token.isNotBlank())
                                            viewModel.updateUser(
                                                token = token,
                                                snackbarHostState,
                                                navController
                                            )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (viewModel.isLoading)
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    else
                                        Text(text = "Perbarui profile", fontFamily = fontFamily)
                                }
                            }
                        }
                    }

                    is ViewUiState.Error -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ErrorWarning()
                        }
                    }
                }
            }
        }
    }
}