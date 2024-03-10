package com.ratioapp.ui.screen.detailphoto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Comment
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.CardComment
import com.ratioapp.ui.components.DialogConfirmDelete
import com.ratioapp.ui.components.ErrorWarning
import com.ratioapp.ui.components.ModalBottomAddPhotoToAlbum
import com.ratioapp.ui.components.PullRefresh
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.Red10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailPhotoViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPhotoMeScreen(navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val viewModel: DetailPhotoViewModel = viewModel()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var scope = rememberCoroutineScope()

    if ((viewModel.photoUiState !is ViewUiState.Success) && (token.isNotEmpty()))
        viewModel.getPhoto(
            token = token,
            photoId = viewModel.photoId,
            snackbarHostState = snackbarHostState
        )

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showBottomSheet && (viewModel.photoUiState is ViewUiState.Success)) {
        ModalBottomAddPhotoToAlbum(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState, photoId = viewModel.photoId,
            albums = (viewModel.photoUiState as ViewUiState.Success).data.albums,
            snackbarHostState = snackbarHostState
        )
    }

    var isOpenDialogConfirmDelete by remember {
        mutableStateOf(false)
    }

    var isLoadingDelete by remember {
        mutableStateOf(false)
    }

    DialogConfirmDelete(
        visible = isOpenDialogConfirmDelete,
        onDismissRequest = { isOpenDialogConfirmDelete = false },
        title = { Text(text = "Hapus Foto") },
        text = { Text(text = "Apakah anda yakin ingin menghapus foto ini?") },
        onConfirmRequest = {
            scope.launch {
                isLoadingDelete = true
                if (token.isNotBlank())
                    scope.launch {
                        try {
                            RatioApi().photoService.delete(
                                photoId = viewModel.photoId,
                                token = "Bearer $token"
                            )
                            navController.navigate(NavigationItem.ProfileMe.route)
                        } catch (e: HttpException) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = "Gagal hapus",
                                withDismissAction = true
                            )
                        } catch (e: IOException) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = "Periksa koneksi",
                                withDismissAction = true
                            )
                        } finally {
                            isLoadingDelete = false
                        }
                    }
            }
        }, isLoading = isLoadingDelete)

    Scaffold(bottomBar = {
        HorizontalDivider(color = Color.LightGray.copy(0.4F))
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null, modifier = Modifier.size(30.dp)
            )
            TextField(
                value = viewModel.comment,
                onValueChange = { viewModel.comment = it },
                placeholder = {
                    Text(
                        text = "Tambahkan komentar",
                        fontFamily = fontFamily,
                        fontSize = 14.sp
                    )
                },
                textStyle = TextStyle(fontFamily = fontFamily),
                maxLines = 2,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ), modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (token.isNotEmpty())
                                viewModel.createComment(token, viewModel.photoId, snackbarHostState)
                        },
                    ) {
                        if (viewModel.commentUiState == ViewUiState.Loading<Comment>(isLoading = true))
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        else
                            Icon(
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                    }
                }
            )
        }
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopAppBar(
            title = { Text(text = "", overflow = TextOverflow.Ellipsis, maxLines = 1) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }, actions = {
                var isOpen by remember {
                    mutableStateOf(false)
                }
                IconButton(onClick = { isOpen = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ellipsis),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false },
                    modifier = Modifier.background(
                        Color.White
                    )
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit", fontFamily = fontFamily) },
                        onClick = { /*TODO*/ },
                        modifier = Modifier.width(160.dp)
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Hapus",
                                fontFamily = fontFamily,
                                color = Red10
                            )
                        },
                        onClick = { isOpenDialogConfirmDelete = true },
                        modifier = Modifier.width(160.dp)
                    )
                }
            })
    }) {
        PullRefresh(
            modifier = Modifier.padding(it),
            onRefresh = {
                if (token.isNotEmpty())
                    viewModel.getPhoto(
                        token = token,
                        photoId = viewModel.photoId,
                        snackbarHostState = snackbarHostState,
                        withPullRefresh = true
                    )
            },
            refreshing = viewModel.photoUiState == ViewUiState.Loading<Photo>(
                isLoading = true,
                withPullRefresh = true
            )
        ) {
            when (viewModel.photoUiState) {
                is ViewUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(top = 100.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ViewUiState.Success -> {
                    val data = (viewModel.photoUiState as ViewUiState.Success<Photo>).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = "${RatioApi.BASE_URL}/files/images/photos/${data.locationFile}",
                            contentDescription = null,
                            placeholder = painterResource(id = R.drawable.image_placeholder),
                            error = painterResource(
                                id = R.drawable.image_error
                            ), modifier = Modifier
                                .fillMaxWidth()
                                .background(Green30),
                            contentScale = ContentScale.FillWidth
                        )
                        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    AsyncImage(
                                        model = "${RatioApi.BASE_URL}/files/images/profiles/${data.user?.photoUrl}",
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100.dp))
                                            .size(width = 40.dp, height = 40.dp)
                                            .border(1.dp, Color.Gray, CircleShape),
                                        placeholder = painterResource(id = R.drawable.image_placeholder),
                                        error = painterResource(id = R.drawable.profile)
                                    )
                                    Column {
                                        Text(
                                            text = data.user?.username ?: "",
                                            fontFamily = fontFamily,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.clickable {
                                                navController.navigate(NavigationItem.ProfileUser.route + "/${data.user?.id}")
                                            }
                                        )
                                        Text(
                                            text = data.user?.email ?: "",
                                            fontFamily = fontFamily,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.clickable {
                                                navController.navigate(
                                                    NavigationItem.ProfileUser.route + "/${data.user?.id}"
                                                )
                                            }
                                        )
                                    }
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedIconButton(
                                        onClick = { showBottomSheet = true },
                                        colors = IconButtonDefaults.outlinedIconButtonColors(
                                            containerColor = if (data.albums.isNotEmpty()) Green10 else Color.White
                                        ),
                                        border = BorderStroke(2.dp, Green30),
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.add_to_gallery),
                                            contentDescription = null,
                                            tint = if (data.albums.isNotEmpty()) Color.White else Green10
                                        )
                                    }

                                    OutlinedIconButton(
                                        onClick = {
                                            viewModel.like(
                                                token,
                                                viewModel.photoId,
                                                snackbarHostState
                                            )
                                        },
                                        border = BorderStroke(2.dp, Green30),
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = if (viewModel.isLiked) R.drawable.love_with_fill else R.drawable.love),
                                            contentDescription = null,
                                            tint = if (viewModel.isLiked) Color.Unspecified else Green10
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = data.title,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data.description,
                                fontFamily = fontFamily,
                                fontSize = 14.sp,
                                lineHeight = 19.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(
                            modifier = Modifier
                                .border(BorderStroke(0.5.dp, Color(0x1A000000)))
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        Text(
                            text = "${data.comentars?.size} Komentar",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                        Column(
                            modifier = Modifier.padding(horizontal = 6.dp),
                        ) {
                            if (!data.comentars.isNullOrEmpty())
                                data.comentars.size.let { it1 ->
                                    repeat(it1) { index ->
                                        CardComment(data.comentars[index])
                                    }
                                }
                            else
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 50.dp, vertical = 40.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Berikan komentar pertama\npada postingan ini",
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

                is ViewUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ErrorWarning()
                    }
                }
            }
        }
    }
}