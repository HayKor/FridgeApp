package com.haykor.fridge.auth.data

import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.LoginRequest
import com.haykor.fridge.core.data.remote.models.Result
import com.haykor.fridge.core.data.remote.util.BearerUtil
import com.haykor.fridge.core.data.remote.util.CookieUtil
import javax.inject.Inject

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
        val response = authService.refreshTokens(
            refreshToken = CookieUtil.createRefreshTokenCookie(refreshToken)
        )
        if (response is Result.Success)
            tokenManager.saveTokens(response.data)
        return response
    }

    override suspend fun logout() {
        val accessToken =
            tokenManager.getAccessToken() ?: return
        authService.logout(BearerUtil.createBearerAccessTokenHeader(accessToken))
        tokenManager.clearTokens()
    }

    override suspend fun isLoggedIn(): Boolean {
        return when {
            hasValidAccessToken() -> true
            canRefreshToken() -> refreshAndValidateToken()
            else -> {
                tokenManager.clearTokens()
                false
            }
        }
    }

    private suspend fun hasValidAccessToken(): Boolean {
        return !tokenManager.isAccessTokenExpired()
    }

    private suspend fun canRefreshToken(): Boolean {
        return !tokenManager.isRefreshTokenExpired() &&
                tokenManager.getRefreshToken() != null
    }

    private suspend fun refreshAndValidateToken(): Boolean {
        val refreshToken = tokenManager.getRefreshToken() ?: return false

        return try {
            val response = refreshTokens(
                refreshToken = CookieUtil.createRefreshTokenCookie(refreshToken)
            )
            when (response) {
                is Result.Success -> {
                    tokenManager.saveTokens(response.data)
                    true
                }

                is Result.Error -> {
                    tokenManager.clearTokens()
                    false
                }
            }
        } catch (e: Exception) {
            tokenManager.clearTokens()
            false // Network error or smth
        }
    }
}