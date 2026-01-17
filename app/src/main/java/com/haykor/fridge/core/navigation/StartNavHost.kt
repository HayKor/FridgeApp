package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.feature.start.presentation.screens.splash.SplashScreen
import kotlinx.serialization.Serializable

@Composable
fun StartNavHost(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController
) {
    val startNavController = rememberNavController()
    NavHost(
        navController = startNavController,
        startDestination = StartScreens.Splash,
        modifier = modifier
    ) {
        composable<StartScreens.Splash> {
            SplashScreen(
                onCheckAuthComplete = { isLoggedIn ->
                    if (isLoggedIn) {
                        rootNavController.navigate(Destinations.Main) {
                            popUpTo(Destinations.Start) { inclusive = true }
                        }
                    } else {
                        rootNavController.navigate(Destinations.Auth) {
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