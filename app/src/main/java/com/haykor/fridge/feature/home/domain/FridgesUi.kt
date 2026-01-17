package com.haykor.fridge.feature.home.domain

import com.haykor.fridge.core.data.remote.models.FridgesDto

data class FridgesUi(
    val id: Int,
    val name: String
)

fun FridgesDto.toUi(): FridgesUi {
    return FridgesUi(
        id = id,
        name = name
    )
}