package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.auth.presentation.screens.login.LoginScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        startDestination = AuthScreens.Login.route,
        route = Destinations.AUTH
    ) {
        composable(AuthScreens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(AuthScreens.Login.route) { inclusive = true }
                    }
                },
            )
        }
    }
}

sealed class AuthScreens(val route: String) {
    object Login : AuthScreens("login")
    object Register : AuthScreens("register")
}