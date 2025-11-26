package com.haykor.fridge.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.haykor.fridge.core.data.remote.models.AuthResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_EXPIRY = longPreferencesKey("access_token_expiry")
        val REFRESH_TOKEN_EXPIRY = longPreferencesKey("refresh_token_expiry")
    }

    suspend fun saveTokens(authResponse: AuthResponse) {
        val accessTokenExpiryTime =
            System.currentTimeMillis() + (authResponse.accessTokenExpiresIn * 60 * 1000)
        val refreshTokenExpiryTime =
            System.currentTimeMillis() + (authResponse.refreshTokenExpiresIn * 24 * 60 * 60 * 1000)

        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = authResponse.accessToken
            preferences[REFRESH_TOKEN] = authResponse.refreshToken
            preferences[ACCESS_TOKEN_EXPIRY] = accessTokenExpiryTime
            preferences[REFRESH_TOKEN_EXPIRY] = refreshTokenExpiryTime
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { it[ACCESS_TOKEN] }.first()
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { it[REFRESH_TOKEN] }.first()
    }

    suspend fun isAccessTokenExpired(): Boolean {
        val expiry = dataStore.data.map { it[ACCESS_TOKEN_EXPIRY] ?: 0L }.first()
        return System.currentTimeMillis() >= expiry - 60000 // 1 minute buffer
    }

    suspend fun isRefreshTokenExpired(): Boolean {
        val expiry = dataStore.data.map { it[REFRESH_TOKEN_EXPIRY] ?: 0L }.first()
        return System.currentTimeMillis() >= expiry
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(ACCESS_TOKEN_EXPIRY)
            preferences.remove(REFRESH_TOKEN_EXPIRY)
        }
    }
}
