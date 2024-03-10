package com.ratioapp.ui.screen

import android.view.View
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.BottomAppBar
import com.ratioapp.ui.components.CardPhoto
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val userIdLoggin = AuthStore(context).getUserId.collectAsState(initial = "").value
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    if (token.isNotEmpty()) viewModel.getData(token, snackbarHostState = snackbarHostState)

    Scaffold(
        bottomBar = {
            BottomAppBar(navController, NavigationItem.Home.route)
        },
        topBar = {
            Surface(shadowElevation = 1.dp) {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .size(35.dp)
                                    .fillMaxWidth(),
                                alignment = Alignment.Center
                            )
                            Text(
                                text = "Ratio",
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(NavigationItem.Chat.route) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chat_bubble_left_right),
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { it ->
        PullRefresh(
            modifier = Modifier.padding(it),
            onRefresh = {
                if (token.isNotEmpty()) viewModel.getData(
                    token,
                    snackbarHostState = snackbarHostState, withPullRefresh = true
                )
            },
            refreshing = viewModel.listPhotoViewUiState == ViewUiState.Loading<List<Photo>>(
                isLoading = true,
                withPullRefresh = true
            )
        ) {
            when (viewModel.listPhotoViewUiState) {
                is ViewUiState.Loading -> {
                    LazyVerticalStaggeredGrid(userScrollEnabled = viewModel.isScroll,
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 20.dp,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues(10.dp),
                        content = {
                            val boxesHeight: IntArray =
                                intArrayOf(250, 200, 140, 300, 200, 140, 160)

                            items(boxesHeight.size) {
                                val infiniteTransition =
                                    rememberInfiniteTransition(label = "")
                                val color by infiniteTransition.animateColor(
                                    initialValue = Green20.copy(alpha = 0.8f),
                                    targetValue = Green20.copy(alpha = 0.2f),
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(
                                            durationMillis = (500 + (it * 100)),
                                            easing = FastOutLinearInEasing
                                        ), repeatMode = RepeatMode.Reverse
                                    ),
                                    label = ""
                                )
                                Box(
                                    modifier = Modifier
                                        .zIndex(-99f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color)
                                        .height(boxesHeight[it].dp)
                                )
                            }
                        }
                    )
                }

                is ViewUiState.Success -> {
                    if (viewModel.listPhotoViewUiState is ViewUiState.Success) {
                        val data =
                            (viewModel.listPhotoViewUiState as ViewUiState.Success)
                        if (data.data.isNotEmpty())
                            LazyVerticalStaggeredGrid(
                                userScrollEnabled = viewModel.isScroll,
                                columns = StaggeredGridCells.Fixed(2),
                                verticalItemSpacing = 20.dp,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                contentPadding = PaddingValues(10.dp),
                                content = {


                                    items(data.data.size) {
                                        CardPhoto(
                                            navController,
                                            data.data[it],
                                            snackbarHostState = snackbarHostState,
                                            token = token, userIdLoggin = userIdLoggin
                                        )
                                    }

                                }
                            )
                        else
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_camera),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp), tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Belum ada yang mengupload \nfoto sama sekali:(",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
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