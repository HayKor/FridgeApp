package com.haykor.fridge.auth.data

import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.LoginRequest
import com.haykor.fridge.core.data.remote.models.Result
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthResponse> {
        val response = authService.login(LoginRequest(email, password))
        if (response is Result.Success)
            tokenManager.saveTokens(response.data)
        return response
    }

    override suspend fun refreshTokens(refreshToken: String): Result<AuthResponse> {
        val refreshToken =
            tokenManager.getRefreshToken() ?: return Result.Error("No refresh token stored")
        val response = authService.refreshTokens(refreshToken = refreshToken)
        if (response is Result.Success)
            tokenManager.saveTokens(response.data)
        return response
    }

    override suspend fun logout() {
        val refreshToken =
            tokenManager.getRefreshToken() ?: return
        authService.logout(refreshToken)
        tokenManager.clearTokens()
    }
}