package com.haykor.fridge.feature.fridge_content.data

import com.haykor.fridge.core.data.remote.api.FridgeProductsService
import com.haykor.fridge.core.data.remote.models.FridgeProductDto
import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.core.data.remote.models.toMap
import com.haykor.fridge.feature.fridge_content.domain.FridgeProduct
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class FridgeProductsRepositoryImpl @Inject constructor(
    private val fridgeProductsService: FridgeProductsService
) : FridgeProductsRepository {

    override suspend fun getFridgeProducts(filters: FridgeProductsFilters): Result<List<FridgeProduct>> {
        return when (val result = fridgeProductsService.getFridgeProducts(filters.toMap())) {
            is Result.Error -> {
                result
            }

            is Result.Success -> {
                val fridgeProducts = result.data.items.map { it.toFridgeProduct() }
                Result.Success(fridgeProducts)
            }
        }
    }

    override suspend fun deleteFridgeProduct(id: Int): Result<Unit> {
        return fridgeProductsService.deleteFridgeProduct(id)
    }
}

@OptIn(ExperimentalTime::class)
private fun FridgeProductDto.toFridgeProduct() = FridgeProduct(
    id = id,
    name = product.productType.name,
    slug = product.productType.slug,
    fridgeId = fridgeId,
    accountType = product.productType.accountType,
    amount = product.amount,
    calories = product.productType.calories,
    manufacturedAt = product.manufacturedAt,
    expPeriod = product.productType.expPeriod
)