package com.haykor.fridge.feature.products.data

import com.haykor.fridge.core.data.remote.models.CreateProductRequest
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.feature.products.domain.Product
import com.haykor.fridge.feature.products.domain.ProductType

interface ProductsRepository {

    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductTypes(): Result<List<ProductType>>
    suspend fun createProduct(product: CreateProductRequest): Result<Unit>
}