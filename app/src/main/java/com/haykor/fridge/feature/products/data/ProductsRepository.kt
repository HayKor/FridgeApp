package com.haykor.fridge.feature.products.data

import com.haykor.fridge.core.data.remote.models.CreateProductRequest
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.core.data.remote.models.ProductTypeDto
import com.haykor.fridge.core.data.remote.models.Result

interface ProductsRepository {

    suspend fun getProducts(): Result<List<ProductDto>>
    suspend fun getProductTypes(): Result<List<ProductTypeDto>>
    suspend fun createProduct(product: CreateProductRequest): Result<Unit>
}