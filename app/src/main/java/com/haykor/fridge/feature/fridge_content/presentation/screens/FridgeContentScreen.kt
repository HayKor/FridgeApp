package com.haykor.fridge.feature.fridge_content.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.haykor.fridge.feature.fridge_content.presentation.FridgeContentLayout

@Composable
fun FridgeContentScreen(
    fridgeName: String,
    viewModel: FridgeContentViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        FridgeContentLayout(
            header = fridgeName,
            productNameFilter = state.productNameFilter,
            onProductNameFilterChange = viewModel::onProductNameFilterChange,
            selectedFilterType = state.filterType,
            onSelectedFilterTypeChange = viewModel::onFilterTypeChange,
            onFridgeProductDelete = viewModel::onFridgeProductDelete,
            isLoading = state.isLoading,
            items = state.items,
            modifier = modifier.fillMaxSize()
        )
    }
}