package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.FridgeProductsResponse
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface FridgeProductsService {

    companion object {
        const val ROUTE = "fridge_products/"
    }

    @GET(ROUTE)
    suspend fun getFridgeProducts(
        @QueryMap filters: Map<String, String>
    ): Result<FridgeProductsResponse>

    @DELETE(ROUTE + "{id}")
    suspend fun deleteFridgeProduct(
        @Path("id") fridgeProductId: Int
    ): Result<Unit>
}