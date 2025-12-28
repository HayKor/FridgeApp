package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.home.presentation.screens.main.MainScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<Destinations.Home>(
        startDestination = HomeScreens.Main,
    ) {
        composable<HomeScreens.Main> {
            MainScreen()
        }
    }
}

sealed class HomeScreens() {
    @Serializable
    object Main : HomeScreens()
}
