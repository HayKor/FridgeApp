package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.CreateProductRequest
import com.haykor.fridge.core.data.remote.models.ProductDto
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsService {

    companion object {
        const val ROUTE = "products/"
    }

    @GET(ROUTE)
    fun getProducts(): Result<List<ProductDto>>

    @POST(ROUTE)
    fun createProduct(request: CreateProductRequest): Result<Unit>
}