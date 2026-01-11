package com.haykor.fridge.core.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FridgesDto(
    val id: Int,
    val name: String,
    @SerialName("owner_id")
    val ownerId: Int
)

@Serializable
data class CreateFridgeRequest(
    val name: String
)