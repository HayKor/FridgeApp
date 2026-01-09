package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier
) {
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        modifier = modifier,
        startDestination = Destinations.Start
    ) {
        composable<Destinations.Start> {
            StartNavHost(rootNavController = rootNavController)
        }
        composable<Destinations.Auth> {
            AuthNavHost(rootNavController = rootNavController)
        }
        composable<Destinations.Main> {
            MainNavHost()
        }
    }
}