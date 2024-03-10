package com.ratioapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ratioapp.models.ViewUiState
import com.ratioapp.ui.theme.Green10
import com.ratioapp.viewModels.ProfileViewModel

@Composable
fun PostProfile(
    navController: NavController,
    viewModel: ProfileViewModel,
    snackbarHostState: SnackbarHostState,
    me: Boolean = false,
    token: String,
    userIdLoggin: String,
    onRefreshProfile: () -> Unit = {}
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 20.dp,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        content = {
            when (viewModel.uiStatePhotos) {
                is ViewUiState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp), color =
                                Green10
                            )
                        }
                    }
                }

                is ViewUiState.Success -> {
                    val data = viewModel.uiStatePhotos as ViewUiState.Success
                    if (data.data.isEmpty())
                        item(span = StaggeredGridItemSpan.FullLine) {
                            Column(
                                modifier = Modifier
                                    .height(300.dp)
                                    .padding(horizontal = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_camera),
                                    contentDescription = null, modifier = Modifier.size(70.dp),
                                    tint = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Postingan",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Upload hasil jepretmu dan pamerkan ke seluruh penjuru dunia",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    else
                        items(data.data) { data ->
                            CardPhoto(
                                navController,
                                data,
                                false,
                                snackbarHostState = snackbarHostState,
                                withDelete = me,
                                token = token,
                                onRefresh = onRefreshProfile,
                                userIdLoggin = userIdLoggin
                            )
                        }
                }

                is ViewUiState.Error -> {}
            }
        })

}