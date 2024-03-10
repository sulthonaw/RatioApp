package com.ratioapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.models.ViewUiState
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormCreateAlbumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSettingProfile(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: FormCreateAlbumViewModel = viewModel()
    val context = LocalContext.current

    val token = AuthStore(context).getToken.collectAsState("").value

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_to_gallery),
                    contentDescription = null, tint = Green10
                )
                Text(
                    text = "Buat Album",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Green10
                )
            }
            Column {
                Text(text = "Judul", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(2.dp))
                OutlinedTextField(value = viewModel.title,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.title = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Green20, focusedBorderColor = Green10
                    ),
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = "Masukkan judul album",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    })
                MessageErrorTextField(message = viewModel.messagesError["title"])
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(text = "Deskripsi", fontFamily = fontFamily, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                OutlinedTextField(value = viewModel.description,
                    textStyle = TextStyle(fontFamily = fontFamily),
                    onValueChange = { viewModel.description = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Green20, focusedBorderColor = Green10
                    ),
                    maxLines = 3,
                    placeholder = {
                        Text(
                            text = "Masukkan deskripsi album",
                            fontFamily = fontFamily,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    })
                MessageErrorTextField(message = viewModel.messagesError["description"])
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.createAlbum(
                        token = token,
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        onDismissRequest = onDismissRequest
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green10
                )
            ) {
                if (viewModel.uiState == ViewUiState.Loading<Nothing>(isLoading = false))
                    Text(text = "Buat Album", fontFamily = fontFamily)
                else CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White, strokeWidth = 2.dp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}