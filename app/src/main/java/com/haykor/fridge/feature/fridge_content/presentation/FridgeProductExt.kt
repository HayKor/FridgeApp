package com.haykor.fridge.feature.fridge_content.presentation

import com.haykor.fridge.core.data.remote.models.AccountType
import com.haykor.fridge.feature.fridge_content.domain.FridgeProduct
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun FridgeProduct.toUi(fridgeName: String): FridgeProductUi {
    val expiryDate = manufacturedAt + expPeriod
    val currentDate = Clock.System.now()
    // TODO: ceil it idk // idk what I meant by 'ceil'
    val daysLeft = expiryDate - currentDate

    return FridgeProductUi(
        id = id,
        name = name,
        slug = slug,
        manufacturedAt = manufacturedAt.toFormattedString(),
        amount = amount,
        accountType = accountType,
        calories = calories,
        daysLeft = daysLeft.toInt(DurationUnit.DAYS),
        fridgeName = fridgeName
    )
}

@OptIn(ExperimentalTime::class)
fun Instant.toFormattedString(): String {
    return this.format(DateTimeComponents.Format {
        day()
        chars(".")
        monthNumber()
        chars(".")
        year()
    })
}

fun AccountType.toMark(): String {
    return when (this) {
        AccountType.WEIGHT -> "кг"
        AccountType.VOLUME -> "л"
    }
}

