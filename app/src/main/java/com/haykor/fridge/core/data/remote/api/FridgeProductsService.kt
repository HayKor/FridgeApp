package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.FridgeProductsFilters
import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap
import com.haykor.fridge.core.data.remote.models.Result


interface FridgeProductsService {

    @GET("/fridge_products")
    suspend fun getFridgeProducts(
        @QueryMap filters: FridgeProductsFilters
    ): Result<FridgeProductsResponse>
}