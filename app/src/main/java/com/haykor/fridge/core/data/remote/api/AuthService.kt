package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.LoginRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Header("user-agent") userAgent: String
    ): AuthResponse

    @POST("auth/refresh_tokens")
    suspend fun refreshTokens(
        @Header("User-Agent") userAgent: String,
        @Header("Cookie") refreshToken: String,
    ): AuthResponse

    @POST("auth/logout")
    suspend fun logout()
}

