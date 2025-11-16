package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destinations.AUTH_GRAPH
    ) {
        authGraph(navController)
    }
}

object Destinations {
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN = "login"
    const val HOME = "home"
}