package com.haykor.fridge.core.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.haykor.fridge.R
import com.haykor.fridge.core.navigation.MainScreens

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier.Companion
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    NavigationBar(
        modifier = modifier
    ) {
        routes.forEach { route ->
            // TODO: сделать чота не на строках надо
            val selected = currentRoute == route.qualifiedName

            NavigationBarItem(
                selected = selected,
                icon = { Icon(painterResource(route.icon), route.label) },
                label = { Text(route.label) },
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid duplicates
                        launchSingleTop = true
                        restoreState = true
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
    @param:DrawableRes val icon: Int
)

private val routes = listOf(
    NavigationRoute(
        route = MainScreens.Home,
        qualifiedName = MainScreens.Home::class.qualifiedName,
        label = "Home",
        icon = R.drawable.home
    ),
    NavigationRoute(
        route = MainScreens.Fridges,
        qualifiedName = MainScreens.Fridges::class.qualifiedName,
        label = "Fridges",
        icon = R.drawable.outline_ac_unit_24
    )
)