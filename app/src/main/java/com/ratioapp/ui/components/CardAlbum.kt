package com.ratioapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Album
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green30
import com.ratioapp.ui.theme.fontFamily

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardAlbum(navController: NavController, album: Album, colorLoading: Color) {
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .border(1.dp, Green30, RoundedCornerShape(8.dp))
                .aspectRatio(1f)
                .padding(4.dp)
                .clickable { navController.navigate(NavigationItem.DetailAlbums.route + "/${album.id}") }
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(
                    4.dp,
                    alignment = Alignment.CenterVertically
                ),
                maxItemsInEachRow = 2
            ) {
                if (album.photos?.isEmpty() == false)
                    album.photos.take(4).map {
                        AsyncImage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .weight(1f)
                                .background(color = colorLoading)
                                .aspectRatio(1f),
                            model = "${RatioApi.BASE_URL}/files/images/photos/${it.locationFile}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop

                        )
                    }
                else
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Tidak ada foto", fontSize = 10.sp)
                    }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = null,
                modifier = Modifier.offset(
                    y = (this.maxHeight.value - 28).dp,
                    x = (this.maxWidth.value - 28).dp
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = album.title,
            fontFamily = fontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}