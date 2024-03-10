package com.ratioapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefresh(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    refreshing: Boolean = false,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)
    Box(modifier = modifier.fillMaxSize().pullRefresh(state)) {
        content()

        PullRefreshIndicator(
            refreshing = refreshing, state = state, modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}