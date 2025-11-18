package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.auth.presentation.screens.login.LoginScreen
import com.haykor.fridge.auth.presentation.screens.registration.RegistrationScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        startDestination = AuthScreens.Login.route,
        route = Destinations.AUTH
    ) {
        composable(AuthScreens.Login.route) {
            LoginScreen(
                onNavigateRegister = {
                    navController.navigate(AuthScreens.Register.route) {
                        popUpTo(AuthScreens.Login.route)
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(AuthScreens.Login.route) { inclusive = true }
                    }
                },
            )
        }
        composable(AuthScreens.Register.route) {
            RegistrationScreen(
                onNavigateLogin = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class AuthScreens(val route: String) {
    object Login : AuthScreens("login")
    object Register : AuthScreens("register")
}