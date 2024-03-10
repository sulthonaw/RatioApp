package com.ratioapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ratioapp.ui.theme.Green10
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.Green30

@Composable
fun BubbleChat(username: String, message: String, me: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (me) Alignment.End else Alignment.Start
    ) {
        Text(text = username, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = if (me) Green30.copy(0.1f) else Green20),
            border = BorderStroke(width = if (me) 1.dp else 0.dp, Green30.copy(0.8f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)) {
                Text(text = message)
            }
        }
    }
}