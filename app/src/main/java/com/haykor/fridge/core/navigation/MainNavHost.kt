package com.haykor.fridge.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.core.components.FridgeAppBottomNavBar
import kotlinx.serialization.Serializable

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier
) {
    val mainNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            FridgeAppBottomNavBar(mainNavController)
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = MainScreens.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<MainScreens.Home> {
                HomeNavHost(mainNavController = mainNavController)
            }
            composable<MainScreens.Fridges> {
                FridgesNavHost(mainNavController = mainNavController)
            }
        }
    }
}

sealed class MainScreens() {
    @Serializable
    object Home : MainScreens()

    @Serializable
    object Fridges : MainScreens()
}