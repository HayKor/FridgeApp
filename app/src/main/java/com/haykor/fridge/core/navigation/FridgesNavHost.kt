package com.haykor.fridge.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.haykor.fridge.core.components.FridgeAppTopNavBar
import com.haykor.fridge.feature.home.presentation.screens.fridges.FridgesAddScreen
import com.haykor.fridge.feature.home.presentation.screens.fridges.FridgesListScreen
import com.haykor.fridge.feature.home.presentation.screens.fridges.FridgesListViewModel
import kotlinx.serialization.Serializable

@Composable
fun FridgesNavHost(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController
) {
    val fridgesNavController = rememberNavController()
    val fridgesListViewModel: FridgesListViewModel = hiltViewModel()

    NavHost(
        navController = fridgesNavController,
        startDestination = FridgesScreens.FridgesList,
        modifier = modifier
    ) {
        composable<FridgesScreens.FridgesList> {
            FridgesListScreen(
                viewModel = fridgesListViewModel,
                onAddFridge = {
                    fridgesNavController.navigate(FridgesScreens.FridgesAdd)
                }
            )
        }
        composable<FridgesScreens.FridgesAdd> {
            Scaffold(
                topBar = {
                    FridgeAppTopNavBar(
                        title = "Создать холодильник",
                        onClick = { fridgesNavController.popBackStack() }
                    )
                },
                contentWindowInsets = WindowInsets()
            ) { paddingValues ->
                FridgesAddScreen(
                    onAddSuccess = {
                        fridgesNavController.popBackStack()
                        fridgesListViewModel.fetch() // refetch on successful creation
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

sealed class FridgesScreens() {
    @Serializable
    object FridgesList : FridgesScreens()

    @Serializable
    object FridgesAdd : FridgesScreens()
}
