package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.feature.home.presentation.screens.main.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun HomeNavHost(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController
) {
    val homeNavController = rememberNavController()
    NavHost(
        navController = homeNavController,
        startDestination = HomeScreens.Main,
        modifier = modifier
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