package com.ratioapp.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.AlbumProfile
import com.ratioapp.ui.components.ModalBottomSettingProfile
import com.ratioapp.ui.components.PostProfile
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUserScreen(navController: NavController, userId: String) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val userIdLoggin = AuthStore(context).getUserId.collectAsState(initial = "").value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    if (token.isNotBlank() && userId.isNotEmpty() && userIdLoggin.isNotBlank()) {
        viewModel.getUser(token, context, userId, snackbarHostState)
        viewModel.getAlbums(token, userId, context, snackbarHostState)
        viewModel.getPhotos(
            context = context,
            token = token,
            snackbarHostState = snackbarHostState,
            userId = userId
        )
        viewModel.getFollowerAndFollowing(token, userId, userIdLoggin)
    }

    var currentMenu by remember {
        mutableStateOf("post")
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 0.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = if (viewModel.uiStateUser is ViewUiState.Success) (viewModel.uiStateUser as ViewUiState.Success).data.username else "",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp),
        )
        {
            when (viewModel.uiStateUser) {
                is ViewUiState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ViewUiState.Success -> {
                    val data = viewModel.uiStateUser as ViewUiState.Success
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        )
                        AsyncImage(
                            model = "${RatioApi.BASE_URL}/files/images/profiles/${data.data.photoUrl}",
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(
                                    CircleShape
                                )
                                .border(1.dp, Color.Gray, CircleShape),
                            placeholder = painterResource(id = R.drawable.image_placeholder)
                        )
                        Text(
                            text = data.data.username,
                            fontFamily = fontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                        Text(
                            text = data.data.fullName,
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = viewModel.following.toString(),
                                    fontFamily = fontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Diikuti",
                                    fontFamily = fontFamily,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = viewModel.follower.toString(),
                                    fontFamily = fontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Pengikut",
                                    fontFamily = fontFamily,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = viewModel.albumSize.toString(),
                                    fontFamily = fontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Album",
                                    fontFamily = fontFamily,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (viewModel.isLoadingFollow)
                                Column(
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                }
                            else
                                Button(
                                    onClick = {
                                        viewModel.followUser(
                                            token = token,
                                            userId = userId,
                                            snackbarHostState = snackbarHostState
                                        )
                                    },
                                    modifier = Modifier.weight(2f),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp),
                                    border = BorderStroke(1.dp, Green10.copy(0.5f)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (viewModel.isFollow) Green10 else Color.White,
                                        contentColor = if (viewModel.isFollow) Color.White else Green10
                                    )
                                ) {

                                    Text(
                                        text = if (viewModel.isFollow) "Mengikuti" else "Ikuti",
                                        fontSize = 13.sp
                                    )
                                }
                            Button(
                                onClick = { navController.navigate(NavigationItem.DetailChat.route + "/${data.data.id}") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                Text(text = "Pesan", fontSize = 13.sp)
                            }
                        }
                        Row() {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        currentMenu = "post"
                                    }
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 8.dp
                                    ),
                                    text = "Post",
                                    fontFamily = fontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (currentMenu == "post") {
                                    Spacer(
                                        modifier = Modifier
                                            .border(2.dp, Green10, RoundedCornerShape(20))
                                            .height(1.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        currentMenu = "album"
                                    }
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 8.dp
                                    ),
                                    text = "Album",
                                    fontFamily = fontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (currentMenu == "album") {
                                    Spacer(
                                        modifier = Modifier
                                            .border(2.dp, Green10, RoundedCornerShape(20))
                                            .height(1.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                        if (currentMenu == "album") AlbumProfile(
                            navController,
                            viewModel = viewModel
                        )
                        else if (currentMenu == "post") PostProfile(
                            navController,
                            viewModel = viewModel,
                            snackbarHostState,
                            token = token,
                            userIdLoggin = userIdLoggin
                        )
                    }
                }

                is ViewUiState.Error -> {}
            }
        }

    }
}