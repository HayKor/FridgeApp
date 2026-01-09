package com.haykor.fridge.home.data

import com.haykor.fridge.core.data.remote.api.FridgeProductsService
import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import com.haykor.fridge.core.data.remote.models.Result
import javax.inject.Inject

class FridgeProductsRepositoryImpl @Inject constructor(
    private val fridgeProductsService: FridgeProductsService
) : FridgeProductsRepository {
    override suspend fun getFridgeProducts(filters: FridgeProductsFilters): Result<FridgeProductsResponse> {
        return fridgeProductsService.getFridgeProducts(filters)
    }
}