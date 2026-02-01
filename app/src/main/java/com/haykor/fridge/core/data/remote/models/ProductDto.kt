@file:OptIn(ExperimentalTime::class)

package com.haykor.fridge.core.data.remote.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProductDto(
    val id: Int,
    val amount: Int,
    @SerialName("product_type")
    val productType: ProductTypeDto,
    @Contextual
    val manufacturedAt: Instant,
)

@Serializable
data class CreateProductRequest(
    val amount: Int,
    @SerialName("product_type_id")
    val productTypeId: Int,
    @SerialName("manufactured_at")
    val manufacturedAt: Instant
)

@Serializable
enum class AccountType {
    @SerialName("weight")
    WEIGHT,

    @SerialName("volume")
    VOLUME,
}