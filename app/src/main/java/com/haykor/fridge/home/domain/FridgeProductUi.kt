package com.haykor.fridge.home.domain

import com.haykor.fridge.core.data.remote.models.AccountType
import com.haykor.fridge.core.data.remote.models.FridgeProductDto
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class FridgeProductUi(
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
fun FridgeProductDto.toUi(): FridgeProductUi {
    val expiryDate = product.manufacturedAt + product.productType.expPeriod
    val currentDate = Clock.System.now()
    // TODO: ceil it idk
    val daysLeft = expiryDate - currentDate

    return FridgeProductUi(
        id = id,
        name = product.productType.name,
        slug = product.productType.slug,
        manufacturedAt = product.manufacturedAt.toFormattedString(),
        amount = product.amount,
        accountType = product.productType.accountType,
        calories = product.productType.calories,
        daysLeft = daysLeft.toInt(DurationUnit.DAYS)
    )
}

@OptIn(ExperimentalTime::class)
private fun Instant.toFormattedString(): String {
    return this.format(DateTimeComponents.Format {
        day()
        chars(".")
        monthNumber()
        chars(".")
        year()
    })
}
