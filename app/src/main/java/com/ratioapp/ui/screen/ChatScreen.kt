package com.ratioapp.ui.screen

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.fontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val userId = AuthStore(context).getUserId.collectAsState(initial = "").value

    var uiState: ViewUiState<List<User>> by remember {
        mutableStateOf(
            ViewUiState.Loading(true)
        )
    }

    if (token.isNotBlank() && userId.isNotBlank())
        scope.launch {
            uiState = try {
                val request = RatioApi().userService.getUsers("Bearer $token")
                ViewUiState.Success(request.data.filter { it.id != userId })
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 1.dp) {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = {
                        Text(
                            text = "Chat",
                            fontFamily = fontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    })
            }
        }
    ) {
        PullRefresh(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), onRefresh = {
                scope.launch {
                    uiState = ViewUiState.Loading(true)
                    uiState = try {
                        val request = RatioApi().userService.getUsers("Bearer $token")
                        ViewUiState.Success(request.data.filter { it.id != userId })
                    } catch (e: HttpException) {
                        ViewUiState.Error(e)
                    } catch (e: IOException) {
                        ViewUiState.Error(e)
                    }
                }
            }, refreshing = uiState is ViewUiState.Loading
        ) {
            when (uiState) {
                is ViewUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Wait...",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }

                is ViewUiState.Success -> {
                    val data = (uiState as ViewUiState.Success).data
                    LazyColumn(content = {
                        items(data.size) { index ->
                            Box(modifier = Modifier.clickable {
                                navController.navigate(
                                    NavigationItem.DetailChat.route + "/${data[index].id}"
                                )
                            }) {
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
                                            text = data[index].username,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = data[index].fullName,
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

                is ViewUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ErrorWarning()
                    }
                }
            }
        }
    }
}