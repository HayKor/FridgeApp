package com.haykor.fridge.core.data.remote.interceptors


import com.haykor.fridge.core.data.local.datastore.TokenManager
import com.haykor.fridge.core.data.remote.api.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip auth for login/refresh endpoints
        if (request.url.encodedPath.contains("auth/")) {
            return chain.proceed(request)
        }

        return runBlocking {
            try {
                var accessToken = tokenManager.getAccessToken()

//                // Refresh token if expired
                if (tokenManager.isAccessTokenExpired() && !tokenManager.isRefreshTokenExpired()) {
                    accessToken = refreshAccessToken()
                }

                if (tokenManager.isAccessTokenExpired() && tokenManager.isRefreshTokenExpired()) {
                    logout()
                    return@runBlocking chain.proceed(request)
                }

                // Proceed with or without token
                if (accessToken != null) {
                    val authenticatedRequest = request.newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                    chain.proceed(authenticatedRequest)
                } else {
                    chain.proceed(request)
                }
            } catch (e: Exception) {
                // If token refresh fails, proceed without auth (will get 401 from server)
                chain.proceed(request)
            }
        }
    }

    private suspend fun refreshAccessToken(): String? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        return try {
            val response = authService.refreshTokens()
            tokenManager.saveTokens(response)
            response.accessToken
        } catch (e: Exception) {
            // Refresh failed - clear tokens
            tokenManager.clearTokens()
            null
        }
    }

    private suspend fun logout() {
        tokenManager.clearTokens()
    }
}
