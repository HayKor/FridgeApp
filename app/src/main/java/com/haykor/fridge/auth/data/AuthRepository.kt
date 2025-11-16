package com.haykor.fridge.auth.data

import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.Result

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<AuthResponse>

    suspend fun refreshTokens(refreshToken: String): Result<AuthResponse>

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}