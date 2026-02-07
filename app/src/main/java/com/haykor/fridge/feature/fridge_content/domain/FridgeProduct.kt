package com.haykor.fridge.feature.fridge_content.domain

import com.haykor.fridge.core.data.remote.models.AccountType
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FridgeProduct(
    val id: Int,
    val name: String,
    val slug: String,
    val fridgeId: Int,
    val accountType: AccountType,
    val amount: Int,
    val calories: Int,
    val manufacturedAt: Instant,
    val expPeriod: Duration
)
