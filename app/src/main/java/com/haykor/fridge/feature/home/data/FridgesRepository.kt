package com.haykor.fridge.feature.home.data

import com.haykor.fridge.core.data.remote.models.CreateFridgeRequest
import com.haykor.fridge.core.data.remote.models.FridgesDto
import com.haykor.fridge.core.data.remote.models.Result

interface FridgesRepository {

    suspend fun getFridges(): Result<List<FridgesDto>>
    suspend fun createFridge(fridge: CreateFridgeRequest): Result<FridgesDto>
    suspend fun deleteFridge(fridgeId: Int): Result<Unit>
}

