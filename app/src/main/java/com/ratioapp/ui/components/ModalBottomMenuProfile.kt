package com.ratioapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.routes.NavigationItem
import com.ratioapp.ui.theme.fontFamily
import com.ratioapp.viewModels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomMenuProfile(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Text(
            text = "Pengaturan",
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(NavigationItem.EditProfile.route)
                    }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pencil_square),
                    contentDescription = null, modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Edit Profile",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.logout(context = context, navController = navController)
                    }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right_start_on_rectangle),
                    contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Red
                )
                Text(
                    text = "Keluar",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )

            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}