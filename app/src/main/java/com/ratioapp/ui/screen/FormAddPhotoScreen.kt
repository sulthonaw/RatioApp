package com.ratioapp.ui.screen

import android.transition.CircularPropagation
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import com.ratioapp.store.AuthStore
import com.ratioapp.ui.components.DialogChoosePhoto
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.FormAddPhotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddPhotoScreen(navController: NavController) {
    val viewModel: FormAddPhotoViewModel = viewModel()
    val context = LocalContext.current
    val token = AuthStore(context).getToken.collectAsState(initial = "").value

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var hasImage by remember {
        mutableStateOf(false)
    }

    var showChoosePhoto by remember {
        mutableStateOf(false)
    }

    DialogChoosePhoto(
        show = showChoosePhoto,
        onDismissRequest = { showChoosePhoto = false },
        callbackUri = {
            viewModel.file.value = it
            hasImage = true
            showChoosePhoto = false
        })


    Scaffold(
        topBar = {
            Surface(shadowElevation = 1.dp) {
                TopAppBar(title = {
                    Text(
                        text = "Buat Postingan",
                        fontFamily = fontFamily,
                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                }, navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_long_left),
                            contentDescription = null
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            HorizontalDivider(color = Color.LightGray.copy(0.4f))
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                Button(
                    onClick = {
                        viewModel.upload(
                            token = token,
                            snackbarHostState,
                            context = context,
                            navController = navController
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    enabled = (((viewModel.file.value != null) && viewModel.title.isNotBlank() && viewModel.description.isNotBlank()))
                ) {
                    if (viewModel.uiState == ViewUiState.Loading<Photo>(isLoading = true))
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp), color = Color.White
                        )
                    else
                        Text(
                            text = "Unggah",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val choosePhotoModifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    showChoosePhoto = true
                }
                .border(1.dp, Green20, RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(200.dp)

            if (hasImage)
                AsyncImage(
                    model = viewModel.file.value,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    error = painterResource(id = R.drawable.image_error),
                    modifier = choosePhotoModifier,
                    contentScale = ContentScale.Inside
                )
            else
                Column(
                    modifier = choosePhotoModifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Pilih Gambar", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Judul")
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { value -> viewModel.title = value },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    Text(text = "Masukkan judul", color = Color.Gray)
                }, maxLines = 1, singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = "Deskripsi")
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { value -> viewModel.description = value },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    Text(text = "Masukkan deskripsi", color = Color.Gray)
                }, maxLines = 3
            )
        }
    }

}