package com.haykor.fridge.feature.home.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.core.data.remote.models.AccountType
import com.haykor.fridge.core.theme.FridgeAppTheme
import com.haykor.fridge.feature.fridge_content.domain.FridgeProduct
import com.haykor.fridge.feature.fridge_content.presentation.FridgeContentLayout
import com.haykor.fridge.feature.fridge_content.presentation.FridgeProductUi
import com.haykor.fridge.feature.fridge_content.presentation.screens.FilterType
import com.haykor.fridge.feature.fridge_content.presentation.toUi
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    MainScreen(
        productNameFilter = state.productNameFilter,
        onProductNameFilterChange = { viewModel.onProductNameFilterChange(it) },
        selectedFilterType = state.filterType,
        onSelectedFilterTypeChange = { viewModel.onFilterTypeChange(it) },
        onFridgeProductDelete = { viewModel.onFridgeProductDelete(it) },
        isLoading = state.isLoading,
        items = state.items,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun MainScreen(
    productNameFilter: String,
    onProductNameFilterChange: (String) -> Unit,
    selectedFilterType: FilterType,
    onSelectedFilterTypeChange: (FilterType) -> Unit,
    onFridgeProductDelete: (FridgeProductUi) -> Unit,
    isLoading: Boolean,
    items: List<FridgeProductUi>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        FridgeContentLayout(
            header = "Продукты в холодильнике",
            productNameFilter = productNameFilter,
            onProductNameFilterChange = onProductNameFilterChange,
            selectedFilterType = selectedFilterType,
            onSelectedFilterTypeChange = onSelectedFilterTypeChange,
            onFridgeProductDelete = onFridgeProductDelete,
            isLoading = isLoading,
            items = items
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(
    device = "spec:parent=pixel_6,navigation=buttons",
    showSystemUi = true
)
@Composable
private fun FridgeScreenPreview() {
    FridgeAppTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            MainScreen(
                items = fridgeProductsListStub().map { it.toUi("My fridge #1") },
                onProductNameFilterChange = { },
                isLoading = false,
                productNameFilter = "",
                selectedFilterType = FilterType.NAME,
                onFridgeProductDelete = { },
                onSelectedFilterTypeChange = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
internal fun fridgeProductsListStub() = listOf(
    FridgeProduct(
        id = 2,
        fridgeId = 1,
        amount = 1,
        manufacturedAt = Clock.System.now() - 1.days,
        name = "Молоко",
        slug = "Молочные продукты",
        accountType = AccountType.VOLUME,
        calories = 100,
        expPeriod = Duration.parse("P3D")
    ),
    FridgeProduct(
        id = 1,
        fridgeId = 1,
        amount = 1,
        manufacturedAt = Clock.System.now() + 2.days,
        name = "Молоко",
        slug = "Молочные продукты",
        accountType = AccountType.VOLUME,
        calories = 100,
        expPeriod = Duration.parse("P3D")
    ),

    FridgeProduct(
        id = 3,
        fridgeId = 1,
        amount = 1,
        manufacturedAt = Clock.System.now() - 5.days,
        name = "Хлебушек",
        slug = "Хлебные изделия",
        accountType = AccountType.VOLUME,
        calories = 100,
        expPeriod = Duration.parse("P3D")
    )
)
