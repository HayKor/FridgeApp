package com.haykor.fridge.feature.products.domain

import com.haykor.fridge.core.data.remote.models.AccountType
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.feature.fridge_content.domain.toFormattedString
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

data class ProductUi(
    val id: Int,
    val name: String,
    val slug: String,
    val manufacturedAt: String,
    val amount: Int,
    val accountType: AccountType,
    val calories: Int,
    val daysLeft: Int
)

@OptIn(ExperimentalTime::class)
fun ProductDto.toUi(): ProductUi {
    val expiryDate = manufacturedAt + productType.expPeriod
    val currentDate = Clock.System.now()
    val daysLeft = expiryDate - currentDate

    return ProductUi(
        id = id,
        name = productType.name,
        slug = productType.slug,
        manufacturedAt = manufacturedAt.toFormattedString(),
        amount = amount,
        accountType = productType.accountType,
        calories = productType.calories,
        daysLeft = daysLeft.toInt(DurationUnit.DAYS)
    )
}
