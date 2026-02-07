package com.haykor.fridge.feature.products.domain

import com.haykor.fridge.core.data.remote.models.AccountType
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Product(
    val id: Int,
    val name: String,
    val slug: String,
    val accountType: AccountType,
    val amount: Int,
    val calories: Int,
    val manufacturedAt: Instant,
    val expPeriod: Duration
)
