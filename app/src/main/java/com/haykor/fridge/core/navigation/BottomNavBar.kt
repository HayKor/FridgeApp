package com.haykor.fridge.core.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    Log.d("NavigationBar", "currentDestination is $currentDestination")
    Log.d("NavigationBar", "currentRoute is $currentRoute")

    NavigationBar(
        modifier = modifier
    ) {
        routes.forEach { route ->
            val selected = currentRoute == route.qualifiedName

            NavigationBarItem(
                selected = selected,
                icon = { Icon(route.icon, null) },
                label = { Text(route.label) },
                onClick = {
                    if (!selected) {
                        navController.navigate(route.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid duplicates
                            launchSingleTop = true
                            restoreState
                        }
                    }
                }
            )
        }
    }
}

private data class NavigationRoute(
    val route: MainScreens,
    val qualifiedName: String?,
    val label: String,
    val icon: ImageVector
)

private val routes = listOf(
    NavigationRoute(
        route = MainScreens.Home,
        qualifiedName = MainScreens.Home::class.qualifiedName,
        label = "Home",
        icon = Icons.Filled.Home,
    )
)