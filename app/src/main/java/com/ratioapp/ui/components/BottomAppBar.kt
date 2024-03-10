package com.ratioapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ratioapp.R
import com.ratioapp.routes.NavigationItem

@Composable
fun BottomAppBar(navController: NavController, currentRoute: String) {
    class MenuBottomBar(val icon: Painter, val route: String)

    val items: List<MenuBottomBar> = listOf(
        MenuBottomBar(
            painterResource(id = R.drawable.home),
            NavigationItem.Home.route
        ),
        MenuBottomBar(
            painterResource(id = R.drawable.wallet),
            NavigationItem.Wallet.route
        ), MenuBottomBar(
            painterResource(id = R.drawable.add),
            NavigationItem.FormAddPhoto.route
        ), MenuBottomBar(
            painterResource(id = R.drawable.account),
            NavigationItem.ProfileMe.route
        )
    )

    var selected by rememberSaveable {
        mutableIntStateOf(items.indexOfFirst { it.route == currentRoute })
    }


    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(68.dp),
    ) {
        items.mapIndexed { index, item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    selected = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(item.icon, contentDescription = null)
                }, colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.LightGray
                )
            )
        }
    }
}
