package com.ratioapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.Album
import com.ratioapp.models.ViewUiState
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.CardPhoto
import com.ratioapp.ui.components.DialogConfirmDelete
import com.ratioapp.ui.components.DialogEditAlbum
import com.ratioapp.ui.theme.Red10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailAlbumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAlbumsScreen(navController: NavController, albumId: String) {
    val context = LocalContext.current
    val viewModel: DetailAlbumViewModel = viewModel()
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val userIdLoggin = AuthStore(context).getUserId.collectAsState(initial = "").value
    var dialogVisible by remember {
        mutableStateOf(false)
    }

    if (token.isNotBlank())
        viewModel.getAlbum(token, albumId)

    var data by mutableStateOf<Album?>(null)
    if (viewModel.getAlbumUiState is ViewUiState.Success)
        data = (viewModel.getAlbumUiState as ViewUiState.Success<Album>).data

    DialogConfirmDelete(
        visible = dialogVisible,
        onDismissRequest = { dialogVisible = false },
        title = { Text(text = "Hapus Album", fontFamily = fontFamily) },
        onConfirmRequest = {
            viewModel.deleteAlbum(
                context = context,
                token = token,
                albumId = albumId,
                navController = navController
            )
        },
        text = { Text(text = "Apakah anda yakin ingin menghapus?", fontFamily = fontFamily) },
        isLoading = viewModel.deleteAlbumUiState == ViewUiState.Loading<Nothing>(true)
    )

    val snackbarHostState by mutableStateOf(
        SnackbarHostState()
    )

    DialogEditAlbum(
        visible = viewModel.showDialogEditAlbum,
        onDismissRequest = { viewModel.showDialogEditAlbum = false },
        onConfirmRequest = { /*TODO*/ },
        viewModel = viewModel,
        snackbarHostState = snackbarHostState
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                data?.title?.let {
                    Text(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = it,
                        fontFamily = fontFamily,
                    )
                }
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron),
                        contentDescription = null
                    )
                }
            }, actions = {
                var isOpen by remember {
                    mutableStateOf(false)
                }
                if (data != null) {
                    if (data!!.user.id == userIdLoggin) {
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
                                onClick = { viewModel.showDialogEditAlbum = true },
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
                                onClick = { dialogVisible = true },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 20.dp,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                content = {
                    when (viewModel.getAlbumUiState) {
                        is ViewUiState.Loading -> {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is ViewUiState.Success -> {
                            if (data != null) {
                                if (data!!.photos?.isNotEmpty() == true)
                                    items(data!!.photos?.size ?: 0) { index ->
                                        CardPhoto(
                                            navController,
                                            data!!.photos?.get(index)!!,
                                            snackbarHostState = snackbarHostState,
                                            token = token, userIdLoggin = userIdLoggin
                                        )
                                    }
                                else
                                    item(span = StaggeredGridItemSpan.FullLine) {
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 30.dp)
                                                .padding(top = 30.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Tidak ada foto yang dimasukkan kedalam album ini",
                                                textAlign = TextAlign.Center,
                                                fontStyle = FontStyle.Italic, color = Color.Gray
                                            )
                                        }
                                    }
                            }
                        }

                        is ViewUiState.Error -> {}
                        else -> {}
                    }

                })
        }
    }
}