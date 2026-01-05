package com.haykor.fridge.core.navigation

import kotlinx.serialization.Serializable

sealed class Destinations() {
    @Serializable
    object Auth : Destinations()

    @Serializable
    object Start : Destinations()

    @Serializable
    object Main : Destinations()
}