package com.haykor.fridge.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.parent?.route

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.Home::class.qualifiedName,
            icon = { Icon(Icons.Filled.Home, null) },
            label = { Text("Home") },
            onClick = {
                navController.navigate(Destinations.Home) {
                    // TODO: might encounter problems with this Destinations.Home
                    popUpTo<Destinations.Home> {
                        saveState = true
                    }
                    // Avoiding duplicates
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
    }
}