package com.haykor.fridge.feature.home.presentation.screens.fridges

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.R
import com.haykor.fridge.core.components.FridgeAppSwipeableCard
import com.haykor.fridge.core.theme.FridgeAppTheme
import com.haykor.fridge.feature.home.domain.FridgesUi

@Composable
fun FridgesListScreen(
    onAddFridge: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FridgesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFridge
            ) {
                Icon(painterResource(R.drawable.baseline_add_24), "Add")
            }
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->
        FridgesListScreen(
            state = state,
            onFridgeDelete = viewModel::onFridgeDelete,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
private fun FridgesListScreen(
    state: FridgesListState,
    onFridgeDelete: (FridgesUi) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is FridgesListState.Loading -> {
            FridgesListLoading(
                modifier = modifier.fillMaxSize()
            )
        }

        is FridgesListState.Displaying -> {
            FridgesListDisplaying(
                fridgesList = state.fridgesList,
                onFridgeDelete = onFridgeDelete,
                modifier = modifier.fillMaxSize()
            )
        }

        is FridgesListState.DisplayingEmpty -> {
            FridgesListDisplayingEmpty(
                modifier = modifier.fillMaxSize()
            )
        }

        is FridgesListState.Error -> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                Toast.makeText(context, state.msg, Toast.LENGTH_LONG).show()
            }
            FridgesListDisplayingEmpty(
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun FridgesListDisplayingEmpty(
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        Text(
            text = "У вас нету холодильников.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun FridgesListDisplaying(
    fridgesList: List<FridgesUi>,
    onFridgeDelete: (FridgesUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(fridgesList, key = { it.id }) { fridge ->
                FridgesCard(
                    fridge = fridge,
                    onFridgeDelete = { onFridgeDelete(fridge) },
                    modifier = Modifier.fillMaxWidth().animateItem()
                )
            }
        }
    }
}

@Composable
private fun FridgesCard(
    fridge: FridgesUi,
    onFridgeDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    FridgeAppSwipeableCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .padding(2.dp),
        onItemDelete = onFridgeDelete,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Icon(painterResource(R.drawable.outline_ac_unit_24), null)
                Text(
                    text = fridge.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
private fun FridgesListLoading(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun FridgesListDisplayingPreview() {
    FridgeAppTheme {
        FridgesListScreen(
            state = FridgesListState.Displaying(
                fridgesList = listOf(
                    FridgesUi(id = 1, name = "Мой холодильник №1"),
                    FridgesUi(id = 2, name = "Мой холодильник №2")
                )
            ),
            onFridgeDelete = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun FridgesListDisplayingEmptyPreview() {
    FridgeAppTheme {
        FridgesListDisplayingEmpty(
            modifier = Modifier.fillMaxSize()
        )
    }
}