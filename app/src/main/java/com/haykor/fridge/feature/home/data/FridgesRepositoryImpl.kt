package com.haykor.fridge.feature.home.data

import com.haykor.fridge.core.data.remote.api.FridgesService
import com.haykor.fridge.core.data.remote.models.CreateFridgeRequest
import com.haykor.fridge.core.data.remote.models.FridgesDto
import com.haykor.fridge.core.data.remote.models.Result
import javax.inject.Inject

class FridgesRepositoryImpl @Inject constructor(
    private val fridgesService: FridgesService
) : FridgesRepository {

    override suspend fun getFridges(): Result<List<FridgesDto>> {
        val response = fridgesService.getFridges()
        return response
    }

    override suspend fun createFridge(fridge: CreateFridgeRequest): Result<FridgesDto> {
        val response = fridgesService.createFridge(fridge)
        return response
    }

    override suspend fun deleteFridge(fridgeId: Int): Result<Unit> {
        val response = fridgesService.deleteFridge(fridgeId)
        return response
    }
}