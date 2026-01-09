package com.haykor.fridge.core.data.remote.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProductDto(
    val id: Int,
    val amount: Int,
    @SerialName("product_type")
    val productType: ProductTypeDto,
    @OptIn(ExperimentalTime::class)
    @Contextual
    val manufacturedAt: Instant,
)

@Serializable
data class ProductTypeDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerialName("account_type")
    val accountType: AccountType,
    val calories: Int,
    @SerialName("exp_period_before_opening") // use this period by default
    val expPeriod: Duration
)

@Serializable
enum class AccountType {
    @SerialName("weight")
    WEIGHT,

    @SerialName("volume")
    VOLUME,
}