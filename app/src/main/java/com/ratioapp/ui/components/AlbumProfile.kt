package com.ratioapp.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.Album
import com.ratioapp.models.ViewUiState
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green20
import com.ratioapp.viewModels.ProfileViewModel

@Composable
fun AlbumProfile(navController: NavController, viewModel: ProfileViewModel) {


    LazyVerticalGrid(modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(bottom = 10.dp),
        content = {
            item(span = { GridItemSpan(2) }) {}
            when (viewModel.uiStateAlbums) {
                is ViewUiState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp), color = Green10
                            )
                        }
                    }
                }

                is ViewUiState.Success -> {
                    val data = viewModel.uiStateAlbums as ViewUiState.Success<List<Album>>
                    if (data.data.isNotEmpty())
                        items(data.data.size) {
                            val infiniteTransition =
                                rememberInfiniteTransition(label = "")
                            val color by infiniteTransition.animateColor(
                                initialValue = Green20.copy(alpha = 0.8f),
                                targetValue = Green20.copy(alpha = 0.2f),
                                animationSpec = infiniteRepeatable(
                                    animation = tween(
                                        durationMillis = (900 + (it * 100)),
                                        easing = FastOutLinearInEasing
                                    ), repeatMode = RepeatMode.Reverse
                                ),
                                label = ""
                            )
                            CardAlbum(navController, data.data[it], colorLoading = color)
                        }
                    else
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .height(300.dp)
                                    .padding(horizontal = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_gallery),
                                    contentDescription = null, modifier = Modifier.size(70.dp),
                                    tint = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Album",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Tambahkan album untuk kumpulkan kenangan manismu",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                        }
                }

                is ViewUiState.Error -> {}
            }
        })
}