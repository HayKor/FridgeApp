package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshTokensService {

    @POST("auth/refresh_tokens")
    suspend fun refreshTokens(
        @Header("Cookie") refreshToken: String,
    ): Result<AuthResponse>
}