package com.ratioapp.ui.screen.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.ratioapp.models.Photo
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.routes.TypeFollow
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.AlbumProfile
import com.ratioapp.ui.components.BottomAppBar
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.components.ModalBottomMenuProfile
import com.ratioapp.ui.components.ModalBottomSettingProfile
import com.ratioapp.ui.components.PostProfile
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMeScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    val userId = AuthStore(context).getUserId.collectAsState(initial = "").value
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    if (token.isNotEmpty() && userId.isNotEmpty()) {
        viewModel.getUser(token, context, userId, snackbarHostState)
        viewModel.getAlbums(token, userId, context, snackbarHostState)
        viewModel.getPhotos(
            context = context,
            token = token,
            snackbarHostState = snackbarHostState,
            userId = userId
        )
        viewModel.getFollowerAndFollowing(token, userId, userId)
    }

    var currentMenu by remember {
        mutableStateOf("post")
    }

    val createAlbumSheetState = rememberModalBottomSheetState()

    var showCreateAlbumBottomSheet by remember {
        mutableStateOf(false)
    }

    val menuProfileSheetState = rememberModalBottomSheetState()

    var showMenuProfileBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showCreateAlbumBottomSheet) {
        ModalBottomSettingProfile(
            onDismissRequest = { showCreateAlbumBottomSheet = false },
            sheetState = createAlbumSheetState,
            navController = navController, snackbarHostState
        )
    }

    if (showMenuProfileBottomSheet) {
        ModalBottomMenuProfile(
            onDismissRequest = { showMenuProfileBottomSheet = false },
            sheetState = menuProfileSheetState, navController = navController
        )
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 0.dp) {
                TopAppBar(title = {
                    Text(
                        text = "Profile",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }, actions = {
                    IconButton(onClick = { showMenuProfileBottomSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_adjustments_horizontal),
                            contentDescription = null
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomAppBar(navController = navController, NavigationItem.ProfileMe.route)
        },
        floatingActionButton = {
            if (currentMenu == "album") {
                FloatingActionButton(onClick = {
                    showCreateAlbumBottomSheet = true
                }, containerColor = Green10) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_to_gallery),
                        contentDescription = null, tint = Color.White
                    )
                }
            } else if (currentMenu == "post")
                FloatingActionButton(onClick = {
                    navController.navigate(NavigationItem.FormAddPhoto.route)
                }, containerColor = Green10) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = null, tint = Color.White
                    )
                }
        }
    ) {
        PullRefresh(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp),
            onRefresh = {
                if (token.isNotEmpty() && userId.isNotEmpty()) {
                    viewModel.getUser(
                        token,
                        context,
                        userId,
                        snackbarHostState,
                        withPullRefresh = true
                    )
                    viewModel.getAlbums(token, userId, context, snackbarHostState)
                    viewModel.getPhotos(
                        context = context,
                        token = token,
                        snackbarHostState = snackbarHostState,
                        userId = userId
                    )
                    viewModel.getFollowerAndFollowing(token, userId, userIdLoggin = userId)
                }
            },
            refreshing = viewModel.uiStateUser == ViewUiState.Loading<User>(
                isLoading = true,
                withPullRefresh = true
            )
        ) {
            Column {
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
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.clickable {
                                            navController.navigate(NavigationItem.DetailFollow.route + "/$userId/${TypeFollow.FOLLOWERS.name}")
                                        }
                                    )
                                    Text(
                                        text = "Diikuti",
                                        fontFamily = fontFamily,
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.clickable {
                                            navController.navigate(NavigationItem.DetailFollow.route + "/$userId/${TypeFollow.FOLLOWERS.name}")
                                        }
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
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.clickable {
                                            navController.navigate(NavigationItem.DetailFollow.route + "/$userId/${TypeFollow.FOLLOWING.name}")
                                        }
                                    )
                                    Text(
                                        text = "Pengikut",
                                        fontFamily = fontFamily,
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        modifier = Modifier.clickable {
                                            navController.navigate(NavigationItem.DetailFollow.route + "/$userId/${TypeFollow.FOLLOWING.name}")
                                        }
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
                                snackbarHostState = snackbarHostState,
                                me = true, token = token, onRefreshProfile = {
                                    viewModel.getPhotos(
                                        context = context,
                                        token = token,
                                        snackbarHostState = snackbarHostState,
                                        userId = userId,
                                    )
                                }, userIdLoggin = userId
                            )
                        }
                    }

                    is ViewUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(top = 18.dp),
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