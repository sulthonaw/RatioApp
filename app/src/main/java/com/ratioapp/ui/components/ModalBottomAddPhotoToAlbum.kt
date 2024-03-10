package com.ratioapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailPhotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomAddPhotoToAlbum(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    photoId: String,
    albums: List<Album>,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value
    val userId = AuthStore(context).getUserId.collectAsState(initial = "").value
    val viewModel: DetailPhotoViewModel = viewModel()

    if (userId.isNotBlank() && token.isNotBlank())
        viewModel.getAlbum(userId = userId, token = token)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gallery),
                    contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Album Kamu",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (viewModel.albumsUiState) {
                    is ViewUiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ViewUiState.Success -> {
                        val data = viewModel.albumsUiState as ViewUiState.Success
                        if (data.data.isNotEmpty() && token.isNotBlank())
                            repeat(data.data.size) { indexParent ->
                                CardAddToAlbum(
                                    data.data[indexParent],
                                    albums,
                                    token = token,
                                    snackbarHostState = snackbarHostState,
                                    photoId = photoId
                                )
                            }
                        else
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(horizontal = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_gallery),
                                    contentDescription = null, modifier = Modifier.size(30.dp),
                                    tint = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Album",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Tambahkan album dari profile",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                    }

                    is ViewUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(horizontal = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ErrorWarning()
                        }
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
