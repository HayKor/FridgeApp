package com.haykor.fridge.feature.products.data

import com.haykor.fridge.core.data.remote.api.ProductTypesService
import com.haykor.fridge.core.data.remote.api.ProductsService
import com.haykor.fridge.core.data.remote.models.CreateProductRequest
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.core.data.remote.models.ProductTypeDto
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.products.domain.Product
import com.haykor.fridge.feature.products.domain.ProductType
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class ProductsRepositoryImpl @Inject constructor(
    private val productsService: ProductsService,
    private val productTypesService: ProductTypesService
) : ProductsRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return when (val result = productsService.getProducts()) {
            is Result.Error -> {
                result
            }

            is Result.Success -> {
                val products = result.data.map { it.toProduct() }
                Result.Success(products)
            }
        }
    }

    override suspend fun getProductTypes(): Result<List<ProductType>> {
        return when (val result = productTypesService.getProductTypes()) {
            is Result.Error -> {
                return result
            }

            is Result.Success -> {
                val productTypes = result.data.map { it.toProductType() }
                Result.Success(productTypes)
            }
        }
    }

    override suspend fun createProduct(product: CreateProductRequest): Result<Unit> {
        return productsService.createProduct(product)
    }
}

@OptIn(ExperimentalTime::class)
private fun ProductDto.toProduct() = Product(
    id = id,
    name = productType.name,
    slug = productType.slug,
    accountType = productType.accountType,
    calories = productType.calories,
    expPeriod = productType.expPeriod,
    amount = amount,
    manufacturedAt = manufacturedAt
)

private fun ProductTypeDto.toProductType() = ProductType(
    id = id,
    name = name
)