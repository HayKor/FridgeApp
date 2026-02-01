package com.haykor.fridge.core.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ProductTypeDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerialName("account_type")
    val accountType: AccountType,
    val calories: Int,
    @SerialName("exp_period_before_opening") // use this period by default
    val expPeriod: Duration
)