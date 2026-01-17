package com.haykor.fridge.feature.home.data

import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import com.haykor.fridge.core.data.remote.models.Result

interface FridgeProductsRepository {

    suspend fun getFridgeProducts(filters: FridgeProductsFilters): Result<FridgeProductsResponse>
}