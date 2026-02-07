package com.haykor.fridge.feature.fridge_content.presentation

import androidx.compose.runtime.Stable
import com.haykor.fridge.core.data.remote.models.AccountType

@Stable
data class FridgeProductUi(
    val id: Int,
    val name: String,
    val slug: String,
    val manufacturedAt: String,
    val amount: Int,
    val accountType: AccountType,
    val calories: Int,
    val daysLeft: Int,
    val fridgeName: String? = null
)