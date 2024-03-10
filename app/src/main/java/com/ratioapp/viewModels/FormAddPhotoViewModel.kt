package com.ratioapp.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ratioapp.api.RatioApi
import com.ratioapp.libs.compressAndConvertToMultipart
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File

class FormAddPhotoViewModel : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var file = MutableLiveData<Uri?>(null)
    var messagesError = mutableStateMapOf<String, String>()
    var uiState: ViewUiState<Photo> by mutableStateOf(ViewUiState.Loading(false))

    fun upload(
        token: String,
        snackbarHostState: SnackbarHostState,
        context: Context,
        navController: NavController
    ) {
        uiState = ViewUiState.Loading(true)
        viewModelScope.launch {
            if (file.value != null) {
                val imagePart = compressAndConvertToMultipart(
                    context = context,
                    uri = file.value!!,
                    partName = "locationFile"
                )

                if (imagePart != null)
                    uiState = try {
                        val title =
                            this@FormAddPhotoViewModel.title.toRequestBody("text/plain".toMediaTypeOrNull())
                        val description =
                            this@FormAddPhotoViewModel.description.toRequestBody("text/plain".toMediaTypeOrNull())
                        val request = RatioApi().photoService.createPhoto(
                            token = "Bearer $token",
                            file = imagePart,
                            title = title, description = description
                        )
                        navController.navigate(NavigationItem.DetailPhotoMe.route + "/${request.data.id}")
                        this@FormAddPhotoViewModel.title = ""
                        this@FormAddPhotoViewModel.description = ""
                        ViewUiState.Success(request.data)
                    } catch (e: HttpException) {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            "Gagal mengunggah gambar",
                            withDismissAction = true
                        )
                        ViewUiState.Error(e)
                    } catch (e: IOException) {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Periksa koneksi", withDismissAction = true)
                        ViewUiState.Error(e)
                    }
            } else {
                snackbarHostState.showSnackbar(
                    message = "Gambar belum terpilih",
                    withDismissAction = true
                )
            }
        }
    }
}