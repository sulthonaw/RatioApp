package com.ratioapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageErrorTextField(message: String?) {
    if (!message.isNullOrEmpty())
        Text(
            text = message,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
}