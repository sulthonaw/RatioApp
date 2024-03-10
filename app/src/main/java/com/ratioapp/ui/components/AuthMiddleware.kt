package com.ratioapp.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.flow.map

@Composable
fun AuthMiddleware(navController: NavController, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val auth = AuthStore(context)
    val isLoggin: Boolean = auth.getToken.map {
        it.isNotBlank()
    }.collectAsState(initial = true).value

    LaunchedEffect(isLoggin) {
        if (!isLoggin) {
            navController.navigate(NavigationItem.Login.route)
        }
    }

    content()
}