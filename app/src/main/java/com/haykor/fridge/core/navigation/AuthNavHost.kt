package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.auth.presentation.screens.login.LoginScreen
import com.haykor.fridge.auth.presentation.screens.registration.RegistrationScreen
import kotlinx.serialization.Serializable

@Composable
fun AuthNavHost(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController
) {
    val authNavController = rememberNavController()
    NavHost(
        navController = authNavController,
        startDestination = AuthScreens.Login,
        modifier = modifier
    ) {
        composable<AuthScreens.Login> {
            LoginScreen(
                onNavigateRegister = {
                    authNavController.navigate(AuthScreens.Register) {
                        popUpTo(AuthScreens.Login)
                    }
                },
                onLoginSuccess = {
                    rootNavController.navigate(Destinations.Main) {
                        popUpTo(Destinations.Auth) { inclusive = true }
                    }
                },
            )
        }
        composable<AuthScreens.Register> {
            RegistrationScreen(
                onNavigateLogin = {
                    authNavController.popBackStack()
                },
                onRegistrationSuccess = {
                    authNavController.popBackStack()
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