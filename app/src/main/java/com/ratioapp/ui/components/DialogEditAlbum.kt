package com.ratioapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ratioapp.libs.whenUiState
import com.ratioapp.models.ViewUiState
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.DetailAlbumViewModel

@Composable
fun DialogEditAlbum(
    viewModel: DetailAlbumViewModel,
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    isLoading: Boolean = false, snackbarHostState: SnackbarHostState
) {
    if (visible)
        whenUiState(
            uiState = viewModel.getAlbumUiState,
            onLoading = { /*TODO*/ },
            onError = { /*TODO*/ }) {
            Dialog(onDismissRequest = onDismissRequest) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Judul")
                        OutlinedTextField(
                            value = viewModel.title,
                            onValueChange = { e -> viewModel.title = e },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontFamily = fontFamily),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Deskripsi")
                        OutlinedTextField(
                            value = viewModel.description,
                            onValueChange = { e -> viewModel.description = e },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontFamily = fontFamily),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Update", fontFamily = fontFamily)
                        }
                    }
                }
            }
        }
}