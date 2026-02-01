package com.haykor.fridge.feature.products.data

import com.haykor.fridge.core.data.remote.api.ProductTypesService
import com.haykor.fridge.core.data.remote.api.ProductsService
import com.haykor.fridge.core.data.remote.models.CreateProductRequest
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.core.data.remote.models.ProductTypeDto
import com.haykor.fridge.core.data.remote.models.Result
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsService: ProductsService,
    private val productTypesService: ProductTypesService
) : ProductsRepository {

    override suspend fun getProducts(): Result<List<ProductDto>> {
        return productsService.getProducts()
    }

    override suspend fun getProductTypes(): Result<List<ProductTypeDto>> {
        return productTypesService.getProductTypes()
    }

    override suspend fun createProduct(product: CreateProductRequest): Result<Unit> {
        return productsService.createProduct(product)
    }
}