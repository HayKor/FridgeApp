package com.haykor.fridge.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.haykor.fridge.auth.presentation.screens.login.LoginScreen
import com.haykor.fridge.auth.presentation.screens.registration.RegistrationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<Destinations.Auth>(
        startDestination = AuthScreens.Login,
    ) {
        composable<AuthScreens.Login> {
            LoginScreen(
                onNavigateRegister = {
                    navController.navigate(AuthScreens.Register) {
                        popUpTo(AuthScreens.Login)
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.Home) {
                        popUpTo(AuthScreens.Login) { inclusive = true }
                    }
                },
            )
        }
        composable<AuthScreens.Register> {
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

sealed class AuthScreens() {
    @Serializable
    object Login : AuthScreens()

    @Serializable
    object Register : AuthScreens()
}