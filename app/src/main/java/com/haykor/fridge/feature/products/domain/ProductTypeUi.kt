package com.haykor.fridge.feature.products.domain

import com.haykor.fridge.core.data.remote.models.ProductTypeDto

data class ProductTypeUi(
    val id: Int,
    val name: String
)

fun ProductTypeDto.toUi() = ProductTypeUi(
    id = id,
    name = name
)