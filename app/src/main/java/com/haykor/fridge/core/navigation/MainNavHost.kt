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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Destinations.Start
    ) {
        startGraph(navController)
        authGraph(navController)
        homeGraph(navController)
    }
}

sealed class Destinations {
    @Serializable
    object Auth : Destinations()

    @Serializable
    object Home : Destinations()

    @Serializable
    object Start : Destinations()
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.Home,
            icon = { Icon(Icons.Filled.Home, null) },
            label = { Text("Home") },
            onClick = {
                navController.navigate(Destinations.Home) {
                    // Pop up to the start destination of the graph (clear back stack)
                    popUpTo(navController.graph.startDestinationId) {
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