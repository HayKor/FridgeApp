package com.haykor.fridge.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.feature.home.presentation.screens.fridges.FridgesAddScreen
import com.haykor.fridge.feature.home.presentation.screens.fridges.FridgesListScreen
import kotlinx.serialization.Serializable

@Composable
fun FridgesNavHost(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController
) {
    val fridgesNavController = rememberNavController()
    NavHost(
        navController = fridgesNavController,
        startDestination = FridgesScreens.FridgesList,
        modifier = modifier
    ) {
        composable<FridgesScreens.FridgesList> {
            FridgesListScreen(
                onAddFridge = {
                    fridgesNavController.navigate(FridgesScreens.FridgesAdd)
                }
            )
        }
        composable<FridgesScreens.FridgesAdd> {
            FridgesAddScreen()
        }
    }
}

sealed class FridgesScreens() {
    @Serializable
    object FridgesList : FridgesScreens()

    @Serializable
    object FridgesAdd : FridgesScreens()
}
