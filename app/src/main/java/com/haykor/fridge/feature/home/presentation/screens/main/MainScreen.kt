package com.haykor.fridge.feature.home.presentation.screens.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.core.components.FridgeAppTextField
import com.haykor.fridge.core.data.remote.models.AccountType
import com.haykor.fridge.core.data.remote.models.FridgeProductDto
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.core.data.remote.models.ProductTypeDto
import com.haykor.fridge.core.theme.FridgeAppTheme
import com.haykor.fridge.feature.home.domain.FridgeProductUi
import com.haykor.fridge.feature.home.domain.toUi
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
        modifier = modifier
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
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FridgeProductsFilterSection(
                productNameFilter = productNameFilter,
                onProductNameFilterChange = onProductNameFilterChange,
                selectedFilterType = selectedFilterType,
                onSelectedFilterTypeChange = onSelectedFilterTypeChange
            )
            Spacer(Modifier.height(8.dp))
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                FridgeProductsList(
                    items = items,
                    productNameFilter = productNameFilter,
                    onItemDelete = onFridgeProductDelete
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeProductsFilterSection(
    productNameFilter: String,
    onProductNameFilterChange: (String) -> Unit,
    selectedFilterType: FilterType,
    onSelectedFilterTypeChange: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        FridgeAppTextField(
            value = productNameFilter,
            onValueChange = { onProductNameFilterChange(it) },
            label = "Поиск",
            leadingIcon = {
                Icon(Icons.Filled.Search, null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = selectedFilterType.displayName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .exposedDropdownSize()
            ) {
                FilterType.entries.forEach { filterType ->
                    DropdownMenuItem(
                        text = { Text(filterType.displayName) },
                        onClick = {
                            onSelectedFilterTypeChange(filterType)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun FridgeProductsList(
    items: List<FridgeProductUi>,
    onItemDelete: (FridgeProductUi) -> Unit,
    productNameFilter: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 4.dp),
        modifier = modifier
    ) {
        items(
            items = items.filter { productNameFilter.lowercase() in it.name.lowercase() },
            key = { it.id }
        ) {
            FridgeProductCard(
                item = it,
                onItemDelete = { onItemDelete(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
fun FridgeProductCard(
    item: FridgeProductUi,
    onItemDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val dismissThreshold = 100.dp

    val offsetX = remember { Animatable(0f) }

    val statusColor = when {
        item.daysLeft <= 0 -> MaterialTheme.colorScheme.errorContainer
        item.daysLeft in (1..2) -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    Box(
        modifier = modifier.padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Red)
        ) {
            Icon(
                Icons.Outlined.Delete,
                null,
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = statusColor
            ),
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val targetValue =
                                (offsetX.value + delta / density.density).coerceAtMost(0f)
                            offsetX.snapTo(targetValue)
                        }
                    },
                    onDragStopped = { velocity ->
                        if (abs(offsetX.value) > with(density) { dismissThreshold.toPx() }) {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = -2000f,
                                    animationSpec = tween(300)
                                )
                                onItemDelete()
                            }
                        } else {
                            scope.launch { offsetX.animateTo(0f, animationSpec = tween(200)) }
                        }
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Тип: ${item.slug}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Дата изготовления: ${item.manufacturedAt}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Количество: ${item.amount} ${item.accountType.toMark()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Калории: ${item.calories}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Осталось: ${item.daysLeft} дней",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
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
        MainScreen(
            items = fridgeProductsListStub().map { it.toUi() },
            onProductNameFilterChange = { },
            isLoading = false,
            productNameFilter = "",
            selectedFilterType = FilterType.NAME,
            onFridgeProductDelete = { },
            onSelectedFilterTypeChange = {},
        )
    }
}

@OptIn(ExperimentalTime::class)
internal fun fridgeProductsListStub() = listOf(
    FridgeProductDto(
        id = 1,
        fridgeId = 1,
        createdAt = Instant.parse("2025-01-07T12:00:00Z"),
        deletedAt = Instant.parse("2025-01-07T12:00:00Z"),
        product = ProductDto(
            id = 1,
            amount = 1,
            manufacturedAt = Clock.System.now() + 2.days,
            productType = ProductTypeDto(
                id = 1,
                name = "Молоко",
                slug = "Молочные продукты",
                accountType = AccountType.VOLUME,
                calories = 100,
                expPeriod = Duration.parse("P3D")
            ),
        ),
    ),

    FridgeProductDto(
        id = 2,
        fridgeId = 1,
        createdAt = Instant.parse("2025-01-07T12:00:00Z"),
        deletedAt = Instant.parse("2025-01-07T12:00:00Z"),
        product = ProductDto(
            id = 1,
            amount = 1,
            manufacturedAt = Clock.System.now() - 1.days,
            productType = ProductTypeDto(
                id = 1,
                name = "Молоко",
                slug = "Молочные продукты",
                accountType = AccountType.VOLUME,
                calories = 100,
                expPeriod = Duration.parse("P3D")
            ),
        ),
    ),

    FridgeProductDto(
        id = 3,
        fridgeId = 1,
        createdAt = Instant.parse("2025-01-07T12:00:00Z"),
        deletedAt = Instant.parse("2025-01-07T12:00:00Z"),
        product = ProductDto(
            id = 1,
            amount = 1,
            manufacturedAt = Clock.System.now() - 5.days,
            productType = ProductTypeDto(
                id = 1,
                name = "Хлебушек",
                slug = "Хлебные изделия",
                accountType = AccountType.VOLUME,
                calories = 100,
                expPeriod = Duration.parse("P3D")
            ),
        ),
    )
)

private fun AccountType.toMark(): String {
    return when (this) {
        AccountType.WEIGHT -> "кг"
        AccountType.VOLUME -> "л"
    }
}
