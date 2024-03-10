package com.ratioapp.ui.screen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.libs.whenUiState
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailFollowViewModel

enum class StateFollow {
    Following, Followers
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFollowScreen(navController: NavController, userId: String, type: String) {
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val viewModel: DetailFollowViewModel = viewModel()

    var stateFollow by remember {
        mutableStateOf(StateFollow.Followers)
    }

    if (token.isNotBlank()) {
        viewModel.getFollowerAndFollowing(token, userId)
        viewModel.getUser(token, userId)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    whenUiState(
                        uiState = viewModel.uiStateUser,
                        onLoading = {
                            Text(
                                text = "wait...",
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onError = {
                            Text(
                                text = "error",
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }) {
                        val data = viewModel.uiStateUser as ViewUiState.Success
                        Text(
                            text = data.data.username,
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
                Row(
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                stateFollow = StateFollow.Following
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (viewModel.uiStateFollowing is ViewUiState.Success) "${(viewModel.uiStateFollowing as ViewUiState.Success).data.size} Diikuti" else ".. Diikuti",
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        if (stateFollow == StateFollow.Following)
                            HorizontalDivider(color = Color.Gray.copy(0.5f))
                        if (stateFollow == StateFollow.Following)
                            HorizontalDivider(color = Color.Gray.copy(0.1f))
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                stateFollow = StateFollow.Followers
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (viewModel.uiStateFollowers is ViewUiState.Success) "${(viewModel.uiStateFollowers as ViewUiState.Success).data.size} Pengikut" else ".. Pengikut",
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        if (stateFollow == StateFollow.Followers)
                            HorizontalDivider(color = Color.Gray.copy(0.5f))
                        if (stateFollow == StateFollow.Followers)
                            HorizontalDivider(color = Color.Gray.copy(0.1f))
                    }
                }
            }
        }
    ) {
        PullRefresh(modifier = Modifier.padding(it), onRefresh = { /*TODO*/ }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (stateFollow) {
                    StateFollow.Followers -> {
                        whenUiState(
                            uiState = viewModel.uiStateFollowers,
                            onLoading = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            onError = {
                                Text(text = "Error")
                            }) {
                            val data = viewModel.uiStateFollowers as ViewUiState.Success
                            LazyColumn(content = {
                                items(data.data.size) { index ->
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { }) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 12.dp
                                            ),
                                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.profile),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .size(50.dp)
                                                    .border(1.dp, Color.LightGray, CircleShape)
                                            )
                                            Column {
                                                Text(
                                                    text = data.data[index].username,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 16.sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = data.data[index].fullName,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            })
                        }

                    }

                    StateFollow.Following -> {
                        whenUiState(
                            uiState = viewModel.uiStateFollowing,
                            onLoading = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            onError = {
                                Text(text = "Error")
                            }) {
                            val data = viewModel.uiStateFollowing as ViewUiState.Success
                            LazyColumn(content = {
                                items(data.data.size) { index ->
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { }) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 12.dp
                                            ),
                                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.profile),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .size(50.dp)
                                                    .border(1.dp, Color.LightGray, CircleShape)
                                            )
                                            Column {
                                                Text(
                                                    text = data.data[index].username,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 16.sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = data.data[index].fullName,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}