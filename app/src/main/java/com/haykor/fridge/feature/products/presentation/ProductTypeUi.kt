package com.haykor.fridge.feature.products.presentation

import androidx.compose.runtime.Immutable
import com.haykor.fridge.feature.products.domain.ProductType

@Immutable
data class ProductTypeUi(
    val id: Int,
    val name: String
)

fun ProductType.toUi() = ProductTypeUi(
    id = id,
    name = name
)