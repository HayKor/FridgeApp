package com.haykor.fridge.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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
            Scaffold(
                bottomBar = {
                    BottomNavBar(navController)
                }
            ) { paddingValues ->
                MainScreen(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

sealed class HomeScreens() {
    @Serializable
    object Main : HomeScreens()
}
