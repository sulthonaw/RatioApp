package com.ratioapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ratioapp.ui.theme.Red10
import com.ratioapp.ui.theme.fontFamily

@Composable
fun DialogConfirmDelete(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onConfirmRequest: () -> Unit,
    isLoading: Boolean = false
) {
    if (visible)
        AlertDialog(onDismissRequest = onDismissRequest, containerColor = Color.White,
            confirmButton = {
                Button(
                    onClick = onConfirmRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = Red10),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (!isLoading)
                        Text(text = "Hapus", fontFamily = fontFamily)
                    else
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White, strokeWidth = 2.dp
                        )
                }
            },
            title = title,
            text = text,
            dismissButton = {
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Batal", fontFamily = fontFamily)
                }
            }
        )
}