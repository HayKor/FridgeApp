package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.CreateFridgeRequest
import com.haykor.fridge.core.data.remote.models.FridgesDto
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FridgesService {

    @GET("fridges/")
    suspend fun getFridges(
    ): Result<List<FridgesDto>>

    @POST("fridges/")
    suspend fun createFridge(
        @Body request: CreateFridgeRequest
    ): Result<FridgesDto>
}
