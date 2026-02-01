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
        return fridgesService.getFridges()
    }

    override suspend fun getFridge(fridgeId: Int): Result<FridgesDto> {
        return fridgesService.getFridge(fridgeId)
    }

    override suspend fun createFridge(fridge: CreateFridgeRequest): Result<FridgesDto> {
        return fridgesService.createFridge(fridge)
    }

    override suspend fun deleteFridge(fridgeId: Int): Result<Unit> {
        return fridgesService.deleteFridge(fridgeId)
    }
}