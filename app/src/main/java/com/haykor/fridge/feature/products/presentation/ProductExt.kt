package com.haykor.fridge.feature.products.presentation

import com.haykor.fridge.feature.fridge_content.presentation.FridgeProductUi
import com.haykor.fridge.feature.fridge_content.presentation.toFormattedString
import com.haykor.fridge.feature.products.domain.Product
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Product.toUi(): FridgeProductUi {
    val expiryDate = manufacturedAt + expPeriod
    val currentDate = Clock.System.now()
    val daysLeft = expiryDate - currentDate

    return FridgeProductUi(
        id = id,
        name = name,
        slug = slug,
        manufacturedAt = manufacturedAt.toFormattedString(),
        amount = amount,
        accountType = accountType,
        calories = calories,
        daysLeft = daysLeft.toInt(DurationUnit.DAYS)
    )
}