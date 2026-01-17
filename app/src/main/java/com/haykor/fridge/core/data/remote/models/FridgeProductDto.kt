package com.haykor.fridge.core.data.remote.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class FridgeProductsFilters(
    val productName: String? = null,
    val productId: Int? = null,
    val fridgeId: Int? = null
)

fun FridgeProductsFilters.toMap(): Map<String, String> {
    return buildMap {
        productName?.let { put("product_name_ilike", it) }
        productId?.let { put("product_id_eq", it.toString()) }
        fridgeId?.let { put("fridge_id_eq", it.toString()) }
    }
}

@OptIn(ExperimentalTime::class)
@Serializable
data class FridgeProductDto(
    val id: Int,
    @SerialName("fridge_id")
    val fridgeId: Int,
    val product: ProductDto,
    @Contextual
    @SerialName("created_at")
    val createdAt: Instant,
    @Contextual
    @SerialName("deleted_at")
    val deletedAt: Instant
)

@Serializable
data class FridgeProductsResponse(
    val items: List<FridgeProductDto>,
    @SerialName("total_items")
    val totalItems: Int,
    @SerialName("total_pages")
    val totalPages: Int
)