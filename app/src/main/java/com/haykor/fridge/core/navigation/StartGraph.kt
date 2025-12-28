package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.start.presentation.screens.splash.SplashScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.startGraph(navController: NavController) {
    navigation<Destinations.Start>(
        startDestination = StartScreens.Splash,
    ) {
        composable<StartScreens.Splash> {
            SplashScreen(
                onCheckAuthComplete = { isLoggedIn ->
                    if (isLoggedIn) {
                        navController.navigate(Destinations.Home) {
                            popUpTo(Destinations.Start) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Destinations.Auth) {
                            popUpTo(Destinations.Start) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

sealed class StartScreens {
    @Serializable
    object Splash : StartScreens()
}