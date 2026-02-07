package com.haykor.fridge.feature.fridge_content.data

import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.fridge_content.domain.FridgeProduct

interface FridgeProductsRepository {

    suspend fun getFridgeProducts(filters: FridgeProductsFilters): Result<List<FridgeProduct>>
    suspend fun deleteFridgeProduct(id: Int): Result<Unit>
}