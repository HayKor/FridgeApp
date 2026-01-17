package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface FridgeProductsService {

    @GET("fridge_products/")
    suspend fun getFridgeProducts(
        @QueryMap filters: Map<String, String>
    ): Result<FridgeProductsResponse>
}