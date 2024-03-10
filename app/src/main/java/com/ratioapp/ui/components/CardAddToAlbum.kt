package com.ratioapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Album
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import kotlinx.coroutines.launch
import okio.IOException

@Composable
fun CardAddToAlbum(
    album: Album,
    savedAlbum: List<Album>,
    token: String,
    photoId: String,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var isAdded by remember {
        mutableStateOf(savedAlbum.any { it.id == album.id })
    }

    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            scope.launch {
                try {
                    val request = RatioApi().albumService.addPhotoToAlbum(
                        token = "Bearer $token",
                        photoId = photoId,
                        albumId = album.id
                    )
                    isAdded = request.data.isAddedToAlbum
                } catch (e: IOException) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Periksa koneksi", withDismissAction = true)
                }

            }
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray.copy(0.8f), RoundedCornerShape(8.dp))
                ) {
                    if (album.photos?.isNotEmpty() == true)
                        AsyncImage(
                            model = "${RatioApi.BASE_URL}/files/images/photos/${album.photos[0].locationFile}",
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            placeholder = painterResource(id = R.drawable.image_placeholder),
                            error = painterResource(id = R.drawable.image_error),
                            contentScale = ContentScale.Crop
                        )
                    else
                        Text(
                            text = "Tidak ada\nfoto",
                            fontSize = 8.sp,
                            modifier = Modifier.align(
                                Alignment.Center
                            ),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                }
                Column(modifier = Modifier.padding(end = 30.dp)) {
                    Text(
                        text = album.title,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = album.description,
                        fontFamily = fontFamily,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                if (isAdded)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Green10)
                    )
            }
        }
    }
}