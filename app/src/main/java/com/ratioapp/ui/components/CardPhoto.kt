package com.ratioapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Photo
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.theme.fontFamily
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardPhoto(
    navController: NavController,
    photo: Photo,
    withProfile: Boolean = true,
    withDelete: Boolean = false,
    snackbarHostState: SnackbarHostState,
    token: String,
    userIdLoggin: String,
    onRefresh: () -> Unit = {}
) {
    var scope = rememberCoroutineScope()
    var deleteConfirm by remember {
        mutableStateOf(false)
    }
    var isLoadingDelete by remember {
        mutableStateOf(false)
    }

    DialogConfirmDelete(
        visible = deleteConfirm,
        onDismissRequest = {
            if (!isLoadingDelete)
                deleteConfirm = false
        },
        title = { Text(text = "Hapus Foto") },
        text = { Text(text = "Apakah anda yakin ingin menghapus foto \"${photo.title}\"") },
        onConfirmRequest = {
            isLoadingDelete = true
            if (token.isNotBlank())
                scope.launch {
                    try {
                        RatioApi().photoService.delete(
                            photoId = photo.id,
                            token = "Bearer $token"
                        )
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Berhasil dihapus")
                        onRefresh()
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
                        deleteConfirm = false
                        isLoadingDelete = false
                    }
                }
        }, isLoading = isLoadingDelete
    )

    Column {
        AsyncImage(
            model = "${RatioApi.BASE_URL}/files/images/photos/${photo.locationFile}",
            contentDescription = null,
            placeholder = painterResource(
                id = R.drawable.image_placeholder
            ), error = painterResource(id = R.drawable.image_error), modifier = Modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .combinedClickable(onLongClick = {
                    if (withDelete)
                        deleteConfirm = true
                }) {
                    if (userIdLoggin != photo.user?.id)
                        navController.navigate(NavigationItem.DetailPhotoUser.route + "/${photo.id}")
                    else
                        navController.navigate(NavigationItem.DetailPhotoMe.route + "/${photo.id}")
                }
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (withProfile)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AsyncImage(
                    model = "${RatioApi.BASE_URL}/files/images/profiles/${photo.user?.photoUrl}",
                    contentDescription = "profile",
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .size(width = 32.dp, height = 32.dp)
                        .border(1.dp, Color.Gray, CircleShape)
                        .combinedClickable(
                            onClick = {
                                if (userIdLoggin != photo.user?.id)
                                    navController.navigate(NavigationItem.ProfileUser.route + "/${photo.user?.id}")
                                else navController.navigate(NavigationItem.ProfileMe.route)
                            }
                        ),
                    placeholder = painterResource(id = R.drawable.ic_user_circle)
                )
                Column {
                    Text(
                        text = photo.user?.username ?: "",
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable {
                            if (userIdLoggin != photo.user?.id)
                                navController.navigate(NavigationItem.ProfileUser.route + "/${photo.user?.id}")
                            else navController.navigate(NavigationItem.ProfileMe.route)
                        }
                    )
                    Text(
                        text = photo.user?.email ?: "",
                        fontFamily = fontFamily,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.clickable {
                            if (userIdLoggin != photo.user?.id)
                                navController.navigate(NavigationItem.ProfileUser.route + "/${photo.user?.id}")
                            else navController.navigate(NavigationItem.ProfileMe.route)
                        }
                    )
                }
            }
    }
}