package com.haykor.fridge.feature.home.presentation.screens.fridges

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.haykor.fridge.core.components.FridgeAppButton
import com.haykor.fridge.core.components.FridgeAppTextField

@Composable
fun FridgesAddScreen(
    onAddSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FridgesAddViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is FridgesAddEvent.AddError -> {
                    Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
                }

                is FridgesAddEvent.AddSuccess -> {
                    onAddSuccess()
                }
            }
        }
    }

    FridgesAddScreen(
        name = state.name,
        onNameChange = viewModel::onNameChange,
        onAddClick = viewModel::createFridge,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun FridgesAddScreen(
    name: String,
    onNameChange: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            FridgeAppTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Название",
                modifier = Modifier.fillMaxWidth()
            )
            FridgeAppButton(
                onClick = onAddClick,
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = "Создать холодильник",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}