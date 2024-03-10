package com.ratioapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Album
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class DetailAlbumViewModel : ViewModel() {
    var showDialogEditAlbum by mutableStateOf(false)
    var getAlbumUiState: ViewUiState<Album> by mutableStateOf(ViewUiState.Loading(isLoading = true))

    var deleteAlbumUiState: ViewUiState<Nothing> by mutableStateOf(ViewUiState.Loading(isLoading = false))

    var title by mutableStateOf("")
    var description by mutableStateOf("")

    fun getAlbum(token: String, albumId: String) {
        viewModelScope.launch {
            getAlbumUiState = try {
                val request = RatioApi().albumService.getDetailAlbumMe("Bearer $token", albumId)
                title =  request.data.title
                description =  request.data.description
                ViewUiState.Success(data = request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }
    }

    fun updateAlbum(token: String, snackbarHostState: SnackbarHostState) {}

    fun deleteAlbum(
        context: Context,
        token: String,
        albumId: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            deleteAlbumUiState = ViewUiState.Loading(isLoading = true)
            deleteAlbumUiState = try {
                val request = RatioApi().albumService.deleteAlbum(token = "Bearer $token", albumId)
                ViewUiState.Loading(false)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                Toast.makeText(context, "Periksa Koneksi", Toast.LENGTH_LONG).show()
                ViewUiState.Error(e)
            }
            if (deleteAlbumUiState == ViewUiState.Loading<Nothing>(false))
                navController.navigate(NavigationItem.ProfileMe.route)
        }
    }
}
