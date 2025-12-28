package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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