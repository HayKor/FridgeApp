package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.start.presentation.screens.splash.SplashScreen

fun NavGraphBuilder.startGraph(navController: NavController) {
    navigation(
        startDestination = StartScreens.Splash.route,
        route = Destinations.START
    ) {
        composable(StartScreens.Splash.route) {
            SplashScreen(
                onCheckAuthComplete = { isLoggedIn ->
                    if (isLoggedIn) {
                        navController.navigate(Destinations.HOME) {
                            popUpTo(Destinations.START) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Destinations.AUTH) {
                            popUpTo(Destinations.START) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

sealed class StartScreens(val route: String) {
    object Splash : StartScreens("splash")
}