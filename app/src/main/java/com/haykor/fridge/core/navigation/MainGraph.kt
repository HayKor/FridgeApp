package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.home.presentation.screens.main.MainScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        startDestination = HomeScreens.Main.route,
        route = Destinations.HOME
    ) {
        composable(HomeScreens.Main.route) {
            MainScreen()
        }
    }
}

sealed class HomeScreens(val route: String) {
    object Main : HomeScreens("main")
}
