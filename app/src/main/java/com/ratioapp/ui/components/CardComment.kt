package com.ratioapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ratioapp.R
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Comment
import com.ratioapp.ui.theme.Green20
import com.ratioapp.ui.theme.fontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardComment(comment: Comment) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = {})
            .padding(6.dp)
    ) {
        AsyncImage(
            model = "${RatioApi.BASE_URL}/files/images/profiles/${comment.user.photoUrl}",
            contentDescription = "profile",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_user_circle),
        )
        Column {
            Text(
                text = comment.user.username,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
            )
            Text(
                text = comment.comentar,
                fontFamily = fontFamily,
                fontSize = 12.sp,
            )
        }
    }
}