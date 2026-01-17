package com.haykor.fridge.core.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.haykor.fridge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeAppTopNavBar(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    painterResource(R.drawable.outline_arrow_back_24),
                    "Go back"
                )
            }
        },
        modifier = modifier
    )
}