package com.haykor.fridge.feature.fridge_content.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haykor.fridge.core.components.FridgeAppSwipeableCard
import com.haykor.fridge.core.components.FridgeAppTextField
import com.haykor.fridge.core.theme.FridgeAppTheme
import com.haykor.fridge.feature.fridge_content.presentation.screens.FilterType
import com.haykor.fridge.feature.home.presentation.screens.main.fridgeProductsListStub
import kotlin.time.ExperimentalTime

@Composable
fun FridgeContentLayout(
    header: String,
    productNameFilter: String,
    onProductNameFilterChange: (String) -> Unit,
    selectedFilterType: FilterType,
    onSelectedFilterTypeChange: (FilterType) -> Unit,
    onFridgeProductDelete: (FridgeProductUi) -> Unit,
    isLoading: Boolean,
    items: List<FridgeProductUi>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        FridgeProductsHeader(header)
        Spacer(Modifier.height(8.dp))
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

@Composable
private fun FridgeProductsHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FridgeProductsFilterSection(
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
                    .menuAnchor(ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable)
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
private fun FridgeProductsList(
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
private fun FridgeProductCard(
    item: FridgeProductUi,
    onItemDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = when {
        item.daysLeft <= 0 -> MaterialTheme.colorScheme.errorContainer
        item.daysLeft in (1..2) -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    FridgeAppSwipeableCard(
        onItemDelete = onItemDelete,
//        colors = CardDefaults.cardColors(
//            containerColor = statusColor
//        ),
        modifier = modifier.padding(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = statusColor,
                shape = CardDefaults.shape
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)

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
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Surface(
                color = statusColor,
                shape = CardDefaults.shape,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = item.fridgeName ?: "неизвестный",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(
    device = "spec:parent=pixel_6,navigation=buttons",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
//@PreviewLightDark
@Composable
private fun FridgeScreenPreview() {
    FridgeAppTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxSize()
        ) {
            FridgeContentLayout(
                header = "Продукты в холодильнике",
                items = fridgeProductsListStub().map { it.toUi("My fridge #1") },
                onProductNameFilterChange = { },
                isLoading = false,
                productNameFilter = "",
                selectedFilterType = FilterType.EXPIRY,
                onFridgeProductDelete = { },
                onSelectedFilterTypeChange = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}