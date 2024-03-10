package com.ratioapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ratioapp.models.ViewUiState
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.BubbleChat
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatScreen(navController: NavController, userId: String) {
    val viewModel: DetailChatViewModel = viewModel()
    val context = LocalContext.current
    val userIdLoggin = AuthStore(context).getUserId.collectAsState(initial = "").value
    val token = AuthStore(context).getToken.collectAsState(initial = "").value

    if (userIdLoggin.isNotBlank()) {
        viewModel.getMessagesReceiver(userIdLoggin)
        viewModel.getMessagesSender(userIdLoggin)
    }


    val messages: List<Map<String, Any>> by viewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    val scrollState = rememberLazyListState()

    val snackbarHostState by mutableStateOf(
        SnackbarHostState()
    )

    if (token.isNotBlank())
        viewModel.getUsers(token, userId)

    Scaffold(
        topBar = {
            Surface(elevation = 1.dp) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Gray, CircleShape)
                                    .size(30.dp)
                            )
                            Text(
                                text = if (viewModel.userUiState is ViewUiState.Success) (viewModel.userUiState as ViewUiState.Success).data.username else "",
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_long_left),
                                contentDescription = null
                            )
                        }
                    })
            }
        },
        bottomBar = {
            HorizontalDivider(color = Color.LightGray.copy(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                        .border(1.dp, Color.Gray, CircleShape)
                )
                OutlinedTextField(
                    value = viewModel.chatMessage,
                    onValueChange = { viewModel.chatMessage = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ), trailingIcon = {
                        IconButton(onClick = {
                            if (userId.isNotBlank())
                                viewModel.addNewMessage(
                                    from = userIdLoggin,
                                    snackbarHostState = snackbarHostState,
                                )
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    placeholder = {
                        Text(text = "Ketik pesan", fontFamily = fontFamily, color = Color.Gray)
                    },
                    textStyle = TextStyle(fontFamily = fontFamily),
                    maxLines = 3
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when (viewModel.userUiState) {
                is ViewUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ViewUiState.Success -> {
                    val user = (viewModel.userUiState as ViewUiState.Success).data
                    LazyColumn(
                        state = scrollState,
                        content = {
                            items(messages.size) { index ->
                                BubbleChat(
                                    username = if (messages[index]["from"].toString() == userIdLoggin) "you" else user.username,
                                    message = messages[index]["message"].toString(),
                                    me = messages[index]["from"].toString() == userIdLoggin
                                )
                            }
                        },
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    )
                }

                is ViewUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
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