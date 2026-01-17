package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.CreateFridgeRequest
import com.haykor.fridge.core.data.remote.models.FridgesDto
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FridgesService {

    companion object {
        const val ROUTE = "fridges/"
    }

    @GET(ROUTE)
    suspend fun getFridges(): Result<List<FridgesDto>>

    @POST(ROUTE)
    suspend fun createFridge(
        @Body request: CreateFridgeRequest
    ): Result<FridgesDto>

    @DELETE(ROUTE + "{id}")
    suspend fun deleteFridge(
        @Path("id") fridgeId: Int
    ): Result<Unit>
}
